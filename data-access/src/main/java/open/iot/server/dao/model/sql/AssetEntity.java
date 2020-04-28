package open.iot.server.dao.model.sql;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.asset.Asset;
import open.iot.server.common.data.id.AssetId;
import open.iot.server.common.data.id.CustomerId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.dao.model.BaseSqlEntity;
import open.iot.server.dao.model.ModelConstants;
import open.iot.server.dao.model.SearchTextEntity;
import open.iot.server.dao.util.mapping.JsonStringType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author james mu
 * @date 19-1-8 下午4:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = ModelConstants.ASSET_COLUMN_FAMILY_NAME)
public final class AssetEntity extends BaseSqlEntity<Asset> implements SearchTextEntity<Asset> {

    @Column(name = ModelConstants.ASSET_TENANT_ID_PROPERTY)
    private String tenantId;

    @Column(name = ModelConstants.ASSET_CUSTOMER_ID_PROPERTY)
    private String customerId;

    @Column(name = ModelConstants.ASSET_NAME_PROPERTY)
    private String name;

    @Column(name = ModelConstants.ASSET_TYPE_PROPERTY)
    private String type;

    @Column(name = ModelConstants.SEARCH_TEXT_PROPERTY)
    private String searchText;

    @Type(type = "json")
    @Column(name = ModelConstants.ASSET_ADDITIONAL_INFO_PROPERTY)
    private JsonNode additionalInfo;

    public AssetEntity() {
        super();
    }

    public AssetEntity(Asset asset) {
        if (asset.getId() != null) {
            this.setId(asset.getId().getId());
        }
        if (asset.getTenantId() != null) {
            this.tenantId = UUIDConverter.fromTimeUUID(asset.getTenantId().getId());
        }
        if (asset.getCustomerId() != null) {
            this.customerId = UUIDConverter.fromTimeUUID(asset.getCustomerId().getId());
        }
        this.name = asset.getName();
        this.type = asset.getType();
        this.additionalInfo = asset.getAdditionalInfo();
    }

    @Override
    public String getSearchTextSource() {
        return name;
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchText() {
        return searchText;
    }

    @Override
    public Asset toData() {
        Asset asset = new Asset(new AssetId(UUIDConverter.fromString(id)));
        asset.setCreatedTime(UUIDs.unixTimestamp(UUIDConverter.fromString(id)));
        if (tenantId != null) {
            asset.setTenantId(new TenantId(UUIDConverter.fromString(tenantId)));
        }
        if (customerId != null) {
            asset.setCustomerId(new CustomerId(UUIDConverter.fromString(customerId)));
        }
        asset.setName(name);
        asset.setType(type);
        asset.setAdditionalInfo(additionalInfo);
        return asset;
    }
}
