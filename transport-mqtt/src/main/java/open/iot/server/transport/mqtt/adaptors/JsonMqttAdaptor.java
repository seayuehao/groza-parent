package open.iot.server.transport.mqtt.adaptors;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import io.netty.handler.codec.mqtt.MqttQoS;
import open.iot.server.common.data.id.SessionId;
import open.iot.server.common.msg.core.AttributesSubscribeMsg;
import open.iot.server.common.msg.core.AttributesUnsubscribeMsg;
import open.iot.server.common.msg.core.AttributesUpdateNotification;
import open.iot.server.common.msg.core.AttributesUpdateRequest;
import open.iot.server.common.msg.core.BasicGetAttributesRequest;
import open.iot.server.common.msg.core.GetAttributesResponse;
import open.iot.server.common.msg.core.ResponseMsg;
import open.iot.server.common.msg.core.RpcSubscribeMsg;
import open.iot.server.common.msg.core.RpcUnsubscribeMsg;
import open.iot.server.common.msg.core.RuleEngineErrorMsg;
import open.iot.server.common.msg.core.TelemetryUploadRequest;
import open.iot.server.common.msg.core.ToDeviceRpcRequestMsg;
import open.iot.server.common.msg.core.ToDeviceRpcResponseMsg;
import open.iot.server.common.msg.core.ToServerRpcResponseMsg;
import open.iot.server.common.msg.kv.AttributesKVMsg;
import open.iot.server.common.msg.session.AdaptorToSessionActorMsg;
import open.iot.server.common.msg.session.BasicAdaptorToSessionActorMsg;
import open.iot.server.common.msg.session.FromDeviceMsg;
import open.iot.server.common.msg.session.SessionActorToAdaptorMsg;
import open.iot.server.common.msg.session.SessionContext;
import open.iot.server.common.msg.session.SessionMsgType;
import open.iot.server.common.msg.session.ToDeviceMsg;
import open.iot.server.common.transport.adaptor.AdaptorException;
import open.iot.server.common.transport.adaptor.JsonConverter;
import open.iot.server.transport.mqtt.MqttTopics;
import open.iot.server.transport.mqtt.MqttTransportHandler;
import open.iot.server.transport.mqtt.session.DeviceSessionCtx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Component("JsonMqttAdaptor")
public class JsonMqttAdaptor implements MqttTransportAdaptor {

    private static final Logger log = LoggerFactory.getLogger("JsonMqttAdaptor");

    private static final Gson GSON = new Gson();

    private static final ByteBufAllocator ALLOCATOR = new UnpooledByteBufAllocator(false);

    @Override
    public AdaptorToSessionActorMsg convertToActorMsg(DeviceSessionCtx ctx, SessionMsgType type, MqttMessage inbound) throws AdaptorException {
        FromDeviceMsg msg;
        switch (type) {
            case POST_TELEMETRY_REQUEST:
                msg = convertToTelemetryUploadRequest(ctx, (MqttPublishMessage) inbound);
                break;
            case POST_ATTRIBUTES_REQUEST:
                msg = convertToUpdateAttributesRequest(ctx, (MqttPublishMessage) inbound);
                break;
            case SUBSCRIBE_ATTRIBUTES_REQUEST:
                msg = new AttributesSubscribeMsg();
                break;
            case UNSUBSCRIBE_ATTRIBUTES_REQUEST:
                msg = new AttributesUnsubscribeMsg();
                break;
            case SUBSCRIBE_RPC_COMMANDS_REQUEST:
                msg = new RpcSubscribeMsg();
                break;
            case UNSUBSCRIBE_RPC_COMMANDS_REQUEST:
                msg = new RpcUnsubscribeMsg();
                break;
            case GET_ATTRIBUTES_REQUEST:
                msg = convertToGetAttributesRequest(ctx, (MqttPublishMessage) inbound);
                break;
            case TO_DEVICE_RPC_RESPONSE:
                msg = convertToRpcCommandResponse(ctx, (MqttPublishMessage) inbound);
                break;
            case TO_SERVER_RPC_REQUEST:
                msg = convertToServerRpcRequest(ctx, (MqttPublishMessage) inbound);
                break;
            default:
                log.warn("[{}] Unsupported msg type: {}!", ctx.getSessionId(), type);
                throw new AdaptorException(new IllegalArgumentException("Unsupported msg type: " + type + "!"));
        }
        return new BasicAdaptorToSessionActorMsg(ctx, msg);
    }

    @Override
    public Optional<MqttMessage> convertToAdaptorMsg(DeviceSessionCtx ctx, SessionActorToAdaptorMsg sessionMsg) throws AdaptorException {
        MqttMessage result = null;
        ToDeviceMsg msg = sessionMsg.getMsg();
        switch (msg.getSessionMsgType()) {
            case STATUS_CODE_RESPONSE:
            case GET_ATTRIBUTES_RESPONSE:
                ResponseMsg<?> responseMsg = (ResponseMsg) msg;
                Optional<Exception> responseError = responseMsg.getError();
                if (responseMsg.isSuccess()) {
                    result = convertResponseMsg(ctx, msg, responseMsg, responseError);
                } else {
                    if (responseError.isPresent()) {
                        throw new AdaptorException(responseError.get());
                    }
                }
                break;
            case ATTRIBUTES_UPDATE_NOTIFICATION:
                AttributesUpdateNotification notification = (AttributesUpdateNotification) msg;
                result = createMqttPublishMsg(ctx, MqttTopics.DEVICE_ATTRIBUTES_TOPIC, notification.getData(), false);
                break;
            case TO_DEVICE_RPC_REQUEST:
                ToDeviceRpcRequestMsg rpcRequest = (ToDeviceRpcRequestMsg) msg;
                result = createMqttPublishMsg(ctx, MqttTopics.DEVICE_RPC_REQUESTS_TOPIC + rpcRequest.getRequestId(),
                        rpcRequest);
                break;
            case TO_SERVER_RPC_RESPONSE:
                ToServerRpcResponseMsg rpcResponse = (ToServerRpcResponseMsg) msg;
                result = createMqttPublishMsg(ctx, MqttTopics.DEVICE_RPC_RESPONSE_TOPIC + rpcResponse.getRequestId(),
                        rpcResponse);
                break;
            case RULE_ENGINE_ERROR:
                RuleEngineErrorMsg errorMsg = (RuleEngineErrorMsg) msg;
                result = createMqttPublishMsg(ctx, "errors", JsonConverter.toErrorJson(errorMsg.getErrorMsg()));
                break;
            default:
                break;
        }
        return Optional.ofNullable(result);
    }

    private MqttMessage convertResponseMsg(DeviceSessionCtx ctx, ToDeviceMsg msg,
                                           ResponseMsg<?> responseMsg, Optional<Exception> responseError) throws AdaptorException {
        MqttMessage result = null;
        SessionMsgType requestMsgType = responseMsg.getRequestMsgType();
        Integer requestId = responseMsg.getRequestId();
        if (requestId >= 0) {
            if (requestMsgType == SessionMsgType.POST_ATTRIBUTES_REQUEST || requestMsgType == SessionMsgType.POST_TELEMETRY_REQUEST) {
                result = MqttTransportHandler.createMqttPubAckMsg(requestId);
            } else if (requestMsgType == SessionMsgType.GET_ATTRIBUTES_REQUEST) {
                GetAttributesResponse response = (GetAttributesResponse) msg;
                Optional<AttributesKVMsg> responseData = response.getData();
                if (response.isSuccess() && responseData.isPresent()) {
                    result = createMqttPublishMsg(ctx,
                            MqttTopics.DEVICE_ATTRIBUTES_RESPONSE_TOPIC_PREFIX + requestId,
                            responseData.get(), true);
                } else {
                    if (responseError.isPresent()) {
                        throw new AdaptorException(responseError.get());
                    }
                }
            }
        }
        return result;
    }

    private MqttPublishMessage createMqttPublishMsg(DeviceSessionCtx ctx, String topic, AttributesKVMsg msg, boolean asMap) {
        return createMqttPublishMsg(ctx, topic, JsonConverter.toJson(msg, asMap));
    }

    private MqttPublishMessage createMqttPublishMsg(DeviceSessionCtx ctx, String topic, ToDeviceRpcRequestMsg msg) {
        return createMqttPublishMsg(ctx, topic, JsonConverter.toJson(msg, false));
    }

    private MqttPublishMessage createMqttPublishMsg(DeviceSessionCtx ctx, String topic, ToServerRpcResponseMsg msg) {
        return createMqttPublishMsg(ctx, topic, JsonConverter.toJson(msg));
    }

    private MqttPublishMessage createMqttPublishMsg(DeviceSessionCtx ctx, String topic, JsonElement json) {
        MqttFixedHeader mqttFixedHeader =
                new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttPublishVariableHeader header = new MqttPublishVariableHeader(topic, ctx.nextMsgId());
        ByteBuf payload = ALLOCATOR.buffer();
        payload.writeBytes(GSON.toJson(json).getBytes(StandardCharsets.UTF_8));
        return new MqttPublishMessage(mqttFixedHeader, header, payload);
    }

    private FromDeviceMsg convertToGetAttributesRequest(DeviceSessionCtx ctx, MqttPublishMessage inbound) throws AdaptorException {
        String topicName = inbound.variableHeader().topicName();
        try {
            Integer requestId = Integer.valueOf(topicName.substring(MqttTopics.DEVICE_ATTRIBUTES_REQUEST_TOPIC_PREFIX.length()));
            String payload = inbound.payload().toString(StandardCharsets.UTF_8);
            JsonElement requestBody = new JsonParser().parse(payload);
            Set<String> clientKeys = toStringSet(requestBody, "clientKeys");
            Set<String> sharedKeys = toStringSet(requestBody, "sharedKeys");
            if (clientKeys == null && sharedKeys == null) {
                return new BasicGetAttributesRequest(requestId);
            } else {
                return new BasicGetAttributesRequest(requestId, clientKeys, sharedKeys);
            }
        } catch (RuntimeException e) {
            log.warn("Failed to decode get attributes request", e);
            throw new AdaptorException(e);
        }
    }

    private FromDeviceMsg convertToRpcCommandResponse(DeviceSessionCtx ctx, MqttPublishMessage inbound) throws AdaptorException {
        String topicName = inbound.variableHeader().topicName();
        try {
            Integer requestId = Integer.valueOf(topicName.substring(MqttTopics.DEVICE_RPC_RESPONSE_TOPIC.length()));
            String payload = inbound.payload().toString(StandardCharsets.UTF_8);
            return new ToDeviceRpcResponseMsg(requestId, payload);
        } catch (RuntimeException e) {
            log.warn("Failed to decode get attributes request", e);
            throw new AdaptorException(e);
        }
    }

    private Set<String> toStringSet(JsonElement requestBody, String name) {
        JsonElement element = requestBody.getAsJsonObject().get(name);
        if (element != null) {
            return new HashSet<>(List.of(element.getAsString().split(",")));
        } else {
            return null;
        }
    }

    private AttributesUpdateRequest convertToUpdateAttributesRequest(SessionContext ctx, MqttPublishMessage inbound) throws AdaptorException {
        String payload = validatePayload(ctx.getSessionId(), inbound.payload());
        try {
            return JsonConverter.convertToAttributes(new JsonParser().parse(payload), inbound.variableHeader().packetId());
        } catch (IllegalStateException | JsonSyntaxException ex) {
            throw new AdaptorException(ex);
        }
    }

    private TelemetryUploadRequest convertToTelemetryUploadRequest(SessionContext ctx, MqttPublishMessage inbound) throws AdaptorException {
        String payload = validatePayload(ctx.getSessionId(), inbound.payload());
        try {
            return JsonConverter.convertToTelemetry(new JsonParser().parse(payload), inbound.variableHeader().packetId());
        } catch (IllegalStateException | JsonSyntaxException ex) {
            throw new AdaptorException(ex);
        }
    }

    private FromDeviceMsg convertToServerRpcRequest(DeviceSessionCtx ctx, MqttPublishMessage inbound) throws AdaptorException {
        String topicName = inbound.variableHeader().topicName();
        String payload = validatePayload(ctx.getSessionId(), inbound.payload());
        try {
            Integer requestId = Integer.valueOf(topicName.substring(MqttTopics.DEVICE_RPC_REQUESTS_TOPIC.length()));
            return JsonConverter.convertToServerRpcRequest(new JsonParser().parse(payload), requestId);
        } catch (IllegalStateException | JsonSyntaxException ex) {
            throw new AdaptorException(ex);
        }
    }

    public static JsonElement validateJsonPayload(SessionId sessionId, ByteBuf payloadData) throws AdaptorException {
        String payload = validatePayload(sessionId, payloadData);
        try {
            return new JsonParser().parse(payload);
        } catch (JsonSyntaxException ex) {
            throw new AdaptorException(ex);
        }
    }

    public static String validatePayload(SessionId sessionId, ByteBuf payloadData) throws AdaptorException {
        try {
            String payload = payloadData.toString(StandardCharsets.UTF_8);
            if (payload == null) {
                log.warn("[{}] Payload is empty!", sessionId.toUidStr());
                throw new AdaptorException(new IllegalArgumentException("Payload is empty!"));
            }
            return payload;
        } finally {
            payloadData.release();
        }
    }
}
