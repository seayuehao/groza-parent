package open.iot.server.common.transport;

import open.iot.server.common.data.Device;
import open.iot.server.common.msg.aware.SessionAwareMsg;

/**
 * @author james mu
 * @date 19-1-21 下午4:36
 */
public interface SessionMsgProcessor {

    void process(SessionAwareMsg msg);

    void onDeviceAdded(Device device);
}
