package open.iot.server.transport.mqtt.session;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import io.netty.handler.codec.mqtt.MqttQoS;
import open.iot.server.common.data.Device;
import open.iot.server.common.data.id.SessionId;
import open.iot.server.common.data.kv.AttributeKvEntry;
import open.iot.server.common.data.kv.KvEntry;
import open.iot.server.common.msg.core.AttributesUpdateNotification;
import open.iot.server.common.msg.core.GetAttributesResponse;
import open.iot.server.common.msg.core.ResponseMsg;
import open.iot.server.common.msg.core.ToDeviceRpcRequestMsg;
import open.iot.server.common.msg.kv.AttributesKVMsg;
import open.iot.server.common.msg.session.SessionActorToAdaptorMsg;
import open.iot.server.common.msg.session.SessionCtrlMsg;
import open.iot.server.common.msg.session.SessionMsgType;
import open.iot.server.common.msg.session.SessionType;
import open.iot.server.common.msg.session.ToDeviceMsg;
import open.iot.server.common.msg.session.ex.SessionException;
import open.iot.server.common.transport.adaptor.JsonConverter;
import open.iot.server.common.transport.session.DeviceAwareSessionContext;
import open.iot.server.transport.mqtt.MqttTopics;
import open.iot.server.transport.mqtt.MqttTransportHandler;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


public class GatewayDeviceSessionCtx extends DeviceAwareSessionContext {

    private static final Gson GSON = new Gson();
    private static final ByteBufAllocator ALLOCATOR = new UnpooledByteBufAllocator(false);
    public static final String DEVICE_PROPERTY = "device";

    private GatewaySessionCtx parent;
    private final MqttSessionId sessionId;
    private volatile boolean closed;
    private AtomicInteger msgIdSeq = new AtomicInteger(0);

    public GatewayDeviceSessionCtx(GatewaySessionCtx parent, Device device) {
        super(parent.getProcessor(), parent.getAuthService(), device);
        this.parent = parent;
        this.sessionId = new MqttSessionId();
    }

    @Override
    public SessionId getSessionId() {
        return sessionId;
    }

    @Override
    public SessionType getSessionType() {
        return SessionType.ASYNC;
    }

    @Override
    public void onMsg(SessionActorToAdaptorMsg sessionMsg) throws SessionException {
        Optional<MqttMessage> message = getToDeviceMsg(sessionMsg);
        message.ifPresent(parent::writeAndFlush);
    }

    private Optional<MqttMessage> getToDeviceMsg(SessionActorToAdaptorMsg sessionMsg) {
        ToDeviceMsg msg = sessionMsg.getMsg();
        switch (msg.getSessionMsgType()) {
            case STATUS_CODE_RESPONSE:
                ResponseMsg<?> responseMsg = (ResponseMsg) msg;
                if (responseMsg.isSuccess()) {
                    SessionMsgType requestMsgType = responseMsg.getRequestMsgType();
                    Integer requestId = responseMsg.getRequestId();
                    if (requestId >= 0 && (requestMsgType == SessionMsgType.POST_ATTRIBUTES_REQUEST || requestMsgType == SessionMsgType.POST_TELEMETRY_REQUEST)) {
                        return Optional.of(MqttTransportHandler.createMqttPubAckMsg(requestId));
                    }
                }
                break;
            case GET_ATTRIBUTES_RESPONSE:
                GetAttributesResponse response = (GetAttributesResponse) msg;
                if (response.isSuccess()) {
                    return Optional.of(createMqttPublishMsg(MqttTopics.GATEWAY_ATTRIBUTES_RESPONSE_TOPIC, response));
                } else {
                    //TODO: push error handling to the gateway
                }
                break;
            case ATTRIBUTES_UPDATE_NOTIFICATION:
                AttributesUpdateNotification notification = (AttributesUpdateNotification) msg;
                return Optional.of(createMqttPublishMsg(MqttTopics.GATEWAY_ATTRIBUTES_TOPIC, notification.getData()));
            case TO_DEVICE_RPC_REQUEST:
                ToDeviceRpcRequestMsg rpcRequest = (ToDeviceRpcRequestMsg) msg;
                return Optional.of(createMqttPublishMsg(MqttTopics.GATEWAY_RPC_TOPIC, rpcRequest));
            default:
                break;
        }
        return Optional.empty();
    }

    @Override
    public void onMsg(SessionCtrlMsg msg) throws SessionException {
        //Do nothing
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public long getTimeout() {
        return 0;
    }

    private MqttMessage createMqttPublishMsg(String topic, GetAttributesResponse response) {
        JsonObject result = new JsonObject();
        result.addProperty("id", response.getRequestId());
        result.addProperty(DEVICE_PROPERTY, device.getName());
        Optional<AttributesKVMsg> responseData = response.getData();
        if (responseData.isPresent()) {
            AttributesKVMsg msg = responseData.get();
            if (msg.getClientAttributes() != null) {
                addValues(result, msg.getClientAttributes());
            }
            if (msg.getSharedAttributes() != null) {
                addValues(result, msg.getSharedAttributes());
            }
        }
        return createMqttPublishMsg(topic, result);
    }

    private void addValues(JsonObject result, List<AttributeKvEntry> kvList) {
        if (kvList.size() == 1) {
            addValueToJson(result, "value", kvList.get(0));
        } else {
            JsonObject values;
            if (result.has("values")) {
                values = result.get("values").getAsJsonObject();
            } else {
                values = new JsonObject();
                result.add("values", values);
            }
            kvList.forEach(value -> addValueToJson(values, value.getKey(), value));
        }
    }

    private void addValueToJson(JsonObject json, String name, KvEntry entry) {
        switch (entry.getDataType()) {
            case BOOLEAN:
                entry.getBooleanValue().ifPresent(aBoolean -> json.addProperty(name, aBoolean));
                break;
            case STRING:
                entry.getStrValue().ifPresent(aString -> json.addProperty(name, aString));
                break;
            case DOUBLE:
                entry.getDoubleValue().ifPresent(aDouble -> json.addProperty(name, aDouble));
                break;
            case LONG:
                entry.getLongValue().ifPresent(aLong -> json.addProperty(name, aLong));
                break;
        }
    }

    private MqttMessage createMqttPublishMsg(String topic, AttributesKVMsg data) {
        JsonObject result = new JsonObject();
        result.addProperty(DEVICE_PROPERTY, device.getName());
        result.add("data", JsonConverter.toJson(data, false));
        return createMqttPublishMsg(topic, result);
    }

    private MqttMessage createMqttPublishMsg(String topic, ToDeviceRpcRequestMsg data) {
        JsonObject result = new JsonObject();
        result.addProperty(DEVICE_PROPERTY, device.getName());
        result.add("data", JsonConverter.toJson(data, true));
        return createMqttPublishMsg(topic, result);
    }

    private MqttPublishMessage createMqttPublishMsg(String topic, JsonElement json) {
        MqttFixedHeader mqttFixedHeader =
                new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttPublishVariableHeader header = new MqttPublishVariableHeader(topic, msgIdSeq.incrementAndGet());
        ByteBuf payload = ALLOCATOR.buffer();
        payload.writeBytes(GSON.toJson(json).getBytes(StandardCharsets.UTF_8));
        return new MqttPublishMessage(mqttFixedHeader, header, payload);
    }
}
