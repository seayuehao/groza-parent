package open.iot.server.dao.model.sql;

import com.fasterxml.jackson.databind.JsonNode;
import open.iot.server.common.data.Device;
import open.iot.server.common.data.id.CustomerId;
import open.iot.server.common.data.id.DeviceId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.dao.model.BaseSqlEntity;
import open.iot.server.dao.model.ModelConstants;
import open.iot.server.dao.model.SearchTextEntity;
import open.iot.server.dao.util.mapping.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;


@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = ModelConstants.DEVICE_COLUMN_FAMILY_NAME)
public final class DeviceEntity extends BaseSqlEntity<Device> implements SearchTextEntity<Device> {

    @Column(name = ModelConstants.DEVICE_TENANT_ID_PROPERTY)
    private String tenantId;

    @Column(name = ModelConstants.DEVICE_CUSTOMER_ID_PROPERTY)
    private String customerId;

    @Column(name = ModelConstants.DEVICE_TYPE_PROPERTY)
    private String type;

    @Column(name = ModelConstants.DEVICE_NAME_PROPERTY)
    private String name;

    @Column(name = ModelConstants.SEARCH_TEXT_PROPERTY)
    private String searchText;

    @Type(type = "json")
    @Column(name = ModelConstants.DEVICE_ADDITIONAL_INFO_PROPERTY)
    private JsonNode additionalInfo;

    public DeviceEntity() {
        super();
    }

    public DeviceEntity(Device device) {
        if (device.getId() != null) {
            this.setId(device.getId().getId());
        }
        if (device.getTenantId() != null) {
            this.tenantId = toString(device.getTenantId().getId());
        }
        if (device.getCustomerId() != null) {
            this.customerId = toString(device.getCustomerId().getId());
        }
        this.name = device.getName();
        this.type = device.getType();
        this.additionalInfo = device.getAdditionalInfo();
    }


    @Override
    public String getSearchTextSource() {
        return name;
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public Device toData() {
        Device device = new Device(new DeviceId(getId()));
        device.setCreatedTime(new Date().getTime());
        if (tenantId != null) {
            device.setTenantId(new TenantId(toUUID(tenantId)));
        }
        if (customerId != null) {
            device.setCustomerId(new CustomerId(toUUID(customerId)));
        }
        device.setName(name);
        device.setType(type);
        device.setAdditionalInfo(additionalInfo);
        return device;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearchText() {
        return searchText;
    }

    public JsonNode getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(JsonNode additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeviceEntity that = (DeviceEntity) o;
        return Objects.equals(tenantId, that.tenantId) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(type, that.type) &&
                Objects.equals(name, that.name) &&
                Objects.equals(searchText, that.searchText) &&
                Objects.equals(additionalInfo, that.additionalInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tenantId, customerId, type, name, searchText, additionalInfo);
    }
}
