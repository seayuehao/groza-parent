package open.iot.server.dao.model.sql;

import open.iot.server.common.data.EntityType;

import java.io.Serializable;

public class AttributeKvCompositeKey implements Serializable {
    private EntityType entityType;
    private String entityId;
    private String attributeType;
    private String attributeKey;

    public AttributeKvCompositeKey() {
    }

    public AttributeKvCompositeKey(EntityType entityType, String entityId, String attributeType, String attributeKey) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.attributeType = attributeType;
        this.attributeKey = attributeKey;
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
}
