package open.iot.server.common.msg.core;

import open.iot.server.common.msg.session.FromDeviceMsg;
import open.iot.server.common.msg.session.SessionMsgType;
import lombok.Data;

/**
 * @author james mu
 * @date 19-1-23 上午10:35
 */
@Data
public class SessionOpenMsg implements FromDeviceMsg {

    @Override
    public SessionMsgType getMsgType() {
        return SessionMsgType.SESSION_OPEN;
    }
}
