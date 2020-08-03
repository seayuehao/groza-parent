package open.iot.server.service.state;

import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import open.iot.server.common.data.Device;
import open.iot.server.common.data.id.DeviceId;
import open.iot.server.dao.device.DeviceService;
import open.iot.server.dao.tenant.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Service
public class DefaultDeviceStateService implements DeviceStateService {

    private ListeningScheduledExecutorService queueExecutor;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private DeviceService deviceService;

    @PostConstruct
    public void init() {
    }

    @PreDestroy
    public void stop() {
        if (queueExecutor != null) {
            queueExecutor.shutdownNow();
        }
    }

    @Override
    public void onDeviceAdded(Device device) {

    }

    @Override
    public void onDeviceUpdated(Device device) {

    }

    @Override
    public void onDeviceDeleted(Device device) {

    }

    @Override
    public void onDeviceConnect(DeviceId deviceId) {

    }

    @Override
    public void onDeviceActivity(DeviceId deviceId) {

    }

    @Override
    public void onDeviceDisConnect(DeviceId deviceId) {

    }

    @Override
    public void onDeviceInactivityTimeoutUpdate(DeviceId deviceId, long inactivityTimeout) {

    }
}
