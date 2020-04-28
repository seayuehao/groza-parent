package open.iot.server.dao.device;

import open.iot.server.common.data.id.DeviceId;
import open.iot.server.common.data.security.DeviceCredentials;

/**
 * @author james mu
 * @date 18-12-25 上午9:10
 */
public interface DeviceCredentialsService {

    DeviceCredentials findDeviceCredentialsByDeviceId(DeviceId deviceId);

    DeviceCredentials findDeviceCredentialsByCredentialsId(String credentialsId);

    DeviceCredentials updateDeviceCredentials(DeviceCredentials deviceCredentials);

    DeviceCredentials createDeviceCredentials(DeviceCredentials deviceCredentials);

    void deleteDeviceCredentials(DeviceCredentials deviceCredentials);
}
