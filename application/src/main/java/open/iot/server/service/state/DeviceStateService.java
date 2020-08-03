package open.iot.server.service.state;

import open.iot.server.common.data.Device;
import open.iot.server.common.data.id.DeviceId;


public interface DeviceStateService {

    void onDeviceAdded(Device device);

    void onDeviceUpdated(Device device);

    void onDeviceDeleted(Device device);

    void onDeviceConnect(DeviceId deviceId);

    void onDeviceActivity(DeviceId deviceId);

    void onDeviceDisConnect(DeviceId deviceId);

    void onDeviceInactivityTimeoutUpdate(DeviceId deviceId, long inactivityTimeout);

}
