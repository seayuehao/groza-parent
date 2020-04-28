package open.iot.server.common.msg.core;

import open.iot.server.common.data.kv.AttributeKvEntry;
import open.iot.server.common.msg.session.FromDeviceRequestMsg;

import java.util.Set;

/**
 * @author james mu
 * @date 18-12-6 下午4:30
 */
public interface AttributesUpdateRequest extends FromDeviceRequestMsg {

    Set<AttributeKvEntry> getAttributes();
}
