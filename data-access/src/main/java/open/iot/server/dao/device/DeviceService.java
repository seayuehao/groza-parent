package open.iot.server.dao.device;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.Device;
import open.iot.server.common.data.EntitySubtype;
import open.iot.server.common.data.device.DeviceSearchQuery;
import open.iot.server.common.data.id.CustomerId;
import open.iot.server.common.data.id.DeviceId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.page.TextPageData;
import open.iot.server.common.data.page.TextPageLink;

import java.util.List;

/**
 * @author james mu
 * @date 18-12-25 上午9:28
 */
public interface DeviceService {

    Device findDeviceById(DeviceId deviceId);

    ListenableFuture<Device> findDeviceByIdAsync(DeviceId deviceId);

    Device findDeviceByTenantIdAndName(TenantId tenantId, String name);

    Device saveDevice(Device device);

    Device assignDeviceToCustomer(DeviceId deviceId, CustomerId customerId);

    Device unassignDeviceFromCustomer(DeviceId deviceId);

    void deleteDevice(DeviceId deviceId);

    TextPageData<Device> findDevicesByTenantId(TenantId tenantId, TextPageLink pageLink);

    TextPageData<Device> findDevicesByTenantIdAndType(TenantId tenantId, String type, TextPageLink pageLink);

    ListenableFuture<List<Device>> findDevicesByTenantIdAndIdsAsync(TenantId tenantId, List<DeviceId> deviceIds);

    void deleteDevicesByTenantId(TenantId tenantId);

    TextPageData<Device> findDevicesByTenantIdAndCustomerId(TenantId tenantId, CustomerId customerId, TextPageLink pageLink);

    TextPageData<Device> findDevicesByTenantIdAndCustomerIdAndType(TenantId tenantId, CustomerId customerId, String type, TextPageLink pageLink);

    ListenableFuture<List<Device>> findDevicesByTenantIdCustomerIdAndIdsAsync(TenantId tenantId, CustomerId customerId, List<DeviceId> deviceIds);

    void unassignCustomerDevices(TenantId tenantId, CustomerId customerId);

    ListenableFuture<List<Device>> findDevicesByQuery(DeviceSearchQuery query);

    ListenableFuture<List<EntitySubtype>> findDeviceTypesByTenantId(TenantId tenantId);
}
