package open.iot.server.service.state;

import open.iot.server.common.data.id.DeviceId;
import open.iot.server.common.data.id.TenantId;


public class DeviceStateData {

    private TenantId tenantId;
    private DeviceId deviceId;

    private DeviceState state;

    public DeviceStateData() {
    }

    public DeviceStateData(TenantId tenantId, DeviceId deviceId, DeviceState state) {
        this.tenantId = tenantId;
        this.deviceId = deviceId;
        this.state = state;
    }


    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public void setDeviceId(DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public DeviceState getState() {
        return state;
    }
}



