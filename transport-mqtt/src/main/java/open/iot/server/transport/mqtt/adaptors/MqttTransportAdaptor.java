package open.iot.server.transport.mqtt.adaptors;

import open.iot.server.common.transport.TransportAdaptor;
import open.iot.server.transport.mqtt.session.DeviceSessionCtx;
import io.netty.handler.codec.mqtt.MqttMessage;


public interface MqttTransportAdaptor extends TransportAdaptor<DeviceSessionCtx, MqttMessage,MqttMessage> {
}
