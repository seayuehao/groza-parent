package open.iot.server.common.transport.auth;

import open.iot.server.common.data.Device;
import open.iot.server.common.data.id.DeviceId;
import open.iot.server.common.data.security.DeviceCredentialsFilter;

import java.util.Optional;

/**
 * @author james mu
 * @date 18-12-21 上午10:50
 */
public interface DeviceAuthService {

    DeviceAuthResult process(DeviceCredentialsFilter credentials);

    Optional<Device> findDeviceById(DeviceId deviceId);
}
