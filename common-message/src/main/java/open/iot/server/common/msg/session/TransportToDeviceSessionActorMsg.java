package open.iot.server.common.msg.session;

import open.iot.server.common.msg.TbActorMsg;
import open.iot.server.common.msg.aware.CustomerAwareMsg;
import open.iot.server.common.msg.aware.DeviceAwareMsg;
import open.iot.server.common.msg.aware.SessionAwareMsg;
import open.iot.server.common.msg.aware.TenantAwareMsg;

/**
 * @author james mu
 * @date 19-1-22 上午9:26
 */
public interface TransportToDeviceSessionActorMsg extends DeviceAwareMsg, CustomerAwareMsg, TenantAwareMsg, SessionAwareMsg, TbActorMsg {

    AdaptorToSessionActorMsg getSessionMsg();

}
