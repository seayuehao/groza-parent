package open.iot.server.dao.sql.device;


import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.Device;
import open.iot.server.common.data.EntitySubtype;
import open.iot.server.common.data.EntityType;
import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.dao.DaoUtil;
import open.iot.server.dao.device.DeviceDao;
import open.iot.server.dao.model.sql.DeviceEntity;
import open.iot.server.dao.sql.JpaAbstractSearchTextDao;
import open.iot.server.dao.util.SqlDao;
import open.iot.server.dao.model.ModelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.*;

import static open.iot.server.common.data.UUIDConverter.fromTimeUUID;
import static open.iot.server.common.data.UUIDConverter.fromTimeUUIDs;


@Component
@SqlDao
public class JpaDeviceDao extends JpaAbstractSearchTextDao<DeviceEntity, Device> implements DeviceDao {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    protected Class<DeviceEntity> getEntityClass() {
        return DeviceEntity.class;
    }

    @Override
    protected CrudRepository<DeviceEntity, String> getCrudRepository() {
        return deviceRepository;
    }

    @Override
    public List<Device> findDevicesByTenantId(UUID tenantId, TextPageLink pageLink) {
        return DaoUtil.convertDataList(
                deviceRepository.findByTenantId(
                        fromTimeUUID(tenantId),
                        Objects.toString(pageLink.getTextSearch(),""),
                        pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                    PageRequest.of(0,pageLink.getLimit())
                )
        );
    }

    @Override
    public List<Device> findDevicesByTenantIdAndType(UUID tenantId, String type, TextPageLink pageLink) {
        return DaoUtil.convertDataList(
                deviceRepository.findByTenantIdAndType(
                        fromTimeUUID(tenantId),
                        type,
                        Objects.toString(pageLink.getTextSearch(), ""),
                        pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                    PageRequest.of(0, pageLink.getLimit())
                )
        );
    }

    @Override
    public ListenableFuture<List<Device>> findDevicesByTenantIdAndIdsAsync(UUID tenantId, List<UUID> deviceIds) {
        return service.submit(() -> DaoUtil.convertDataList(deviceRepository.findDevicesByTenantIdAndIdIn(UUIDConverter.fromTimeUUID(tenantId),fromTimeUUIDs(deviceIds))));
    }

    @Override
    public List<Device> findDevicesByTenantIdAndCustomerId(UUID tenantId, UUID customerId, TextPageLink pageLink) {
        return DaoUtil.convertDataList(
                deviceRepository.findByTenantIdAndCustomerId(
                        fromTimeUUID(tenantId),
                        fromTimeUUID(customerId),
                        Objects.toString(pageLink.getTextSearch(),""),
                        pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                    PageRequest.of(0,pageLink.getLimit())
                )
        );
    }

    @Override
    public List<Device> findDevicesByTenantIdAndCustomerIdAndType(UUID tenantId, UUID customerId, String type, TextPageLink pageLink) {
        return DaoUtil.convertDataList(
                deviceRepository.findByTenantIdAndCustomerIdAndType(
                        fromTimeUUID(tenantId),
                        fromTimeUUID(customerId),
                        type,
                        Objects.toString(pageLink.getTextSearch(),""),
                        pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                    PageRequest.of(0,pageLink.getLimit())
                )
        );
    }

    @Override
    public ListenableFuture<List<Device>> findDevicesByTenantIdCustomerIdAndIdsAsync(UUID tenantId, UUID customerId, List<UUID> deviceIds) {
        return service.submit(() -> DaoUtil.convertDataList(
                deviceRepository.findDevicesByTenantIdAndCustomerIdAndIdIn(fromTimeUUID(tenantId),fromTimeUUID(customerId),fromTimeUUIDs(deviceIds))
        ));
    }

    @Override
    public Optional<Device> findDeviceByTenantIdAndName(UUID tenantId, String name) {
        Device device = DaoUtil.getData(deviceRepository.findByTenantIdAndName(fromTimeUUID(tenantId), name));
        return Optional.ofNullable(device);
    }

    @Override
    public ListenableFuture<List<EntitySubtype>> findTenantDeviceTypesAsync(UUID tenantId) {
        return service.submit(() -> convertTenantDeviceTypesToDto(tenantId, deviceRepository.findTenantDeviceTypes(fromTimeUUID(tenantId))));
    }

    private List<EntitySubtype> convertTenantDeviceTypesToDto(UUID tenantId, List<String> types) {
        List<EntitySubtype> list = Collections.emptyList();
        if (types != null && !types.isEmpty()) {
            list = new ArrayList<>();
            for (String type : types) {
                list.add(new EntitySubtype(new TenantId(tenantId), EntityType.DEVICE, type));
            }
        }
        return list;
    }

}
