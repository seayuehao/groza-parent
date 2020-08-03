package open.iot.server.dao.model.sql;

import open.iot.server.common.data.EntityType;
import open.iot.server.common.data.kv.AttributeKvEntry;
import open.iot.server.common.data.kv.BaseAttributeKvEntry;
import open.iot.server.common.data.kv.BooleanDataEntry;
import open.iot.server.common.data.kv.DoubleDataEntry;
import open.iot.server.common.data.kv.KvEntry;
import open.iot.server.common.data.kv.LongDataEntry;
import open.iot.server.common.data.kv.StringDataEntry;
import open.iot.server.dao.model.ToData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

import static open.iot.server.dao.model.ModelConstants.ATTRIBUTE_KEY_COLUMN;
import static open.iot.server.dao.model.ModelConstants.ATTRIBUTE_TYPE_COLUMN;
import static open.iot.server.dao.model.ModelConstants.BOOLEAN_VALUE_COLUMN;
import static open.iot.server.dao.model.ModelConstants.DOUBLE_VALUE_COLUMN;
import static open.iot.server.dao.model.ModelConstants.ENTITY_ID_COLUMN;
import static open.iot.server.dao.model.ModelConstants.ENTITY_TYPE_COLUMN;
import static open.iot.server.dao.model.ModelConstants.LAST_UPDATE_TS_COLUMN;
import static open.iot.server.dao.model.ModelConstants.LONG_VALUE_COLUMN;
import static open.iot.server.dao.model.ModelConstants.STRING_VALUE_COLUMN;

/**
 * @author james mu
 * @date 18-12-13 上午10:10
 */
@Entity
@Table(name = "attribute_kv")
@IdClass(AttributeKvCompositeKey.class)
public class AttributeKvEntity implements ToData<AttributeKvEntry>, Serializable {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = ENTITY_TYPE_COLUMN)
    private EntityType entityType;

    @Id
    @Column(name = ENTITY_ID_COLUMN)
    private String entityId;

    @Id
    @Column(name = ATTRIBUTE_TYPE_COLUMN)
    private String attributeType;

    @Id
    @Column(name = ATTRIBUTE_KEY_COLUMN)
    private String attributeKey;

    @Column(name = BOOLEAN_VALUE_COLUMN)
    private Boolean booleanValue;

    @Column(name = STRING_VALUE_COLUMN)
    private String strValue;

    @Column(name = LONG_VALUE_COLUMN)
    private Long longValue;

    @Column(name = DOUBLE_VALUE_COLUMN)
    private Double doubleValue;

    @Column(name = LAST_UPDATE_TS_COLUMN)
    private Long lastUpdateTs;

    @Override
    public AttributeKvEntry toData() {
        KvEntry kvEntry = null;
        if (strValue != null) {
            kvEntry = new StringDataEntry(attributeKey, strValue);
        } else if (booleanValue != null) {
            kvEntry = new BooleanDataEntry(attributeKey, booleanValue);
        } else if (doubleValue != null) {
            kvEntry = new DoubleDataEntry(attributeKey, doubleValue);
        } else if (longValue != null) {
            kvEntry = new LongDataEntry(attributeKey, longValue);
        }
        return new BaseAttributeKvEntry(kvEntry, lastUpdateTs);
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public Long getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Long lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }
}
