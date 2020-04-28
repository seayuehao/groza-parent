package open.iot.server.common.data.relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import open.iot.server.common.data.SearchTextBasedWithAdditionalInfo;
import open.iot.server.common.data.id.EntityId;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author james mu
 * @date 19-1-4 上午9:46
 */
@Slf4j
public class EntityRelation implements Serializable {

    public static final long serialVersionUID = 2807343040519543363L;

    public static final String CONTAINS_TYPE = "Contains";
    public static final String MANAGES_TYPE = "Manages";

    private EntityId from;
    private EntityId to;
    private String type;
    private RelationTypeGroup typeGroup;
    private transient JsonNode additionalInfo;
    @JsonIgnore
    private byte[] additionalInfoBytes;

    public EntityRelation() {
        super();
    }

    public EntityRelation(EntityId from, EntityId to, String type) {
        this(from, to, type, RelationTypeGroup.COMMON);
    }

    public EntityRelation(EntityId from, EntityId to, String type, RelationTypeGroup typeGroup) {
        this(from, to, type, typeGroup, null);
    }

    public EntityRelation(EntityId from, EntityId to, String type, RelationTypeGroup typeGroup, JsonNode additionalInfo) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.typeGroup = typeGroup;
        this.additionalInfo = additionalInfo;
    }

    public EntityRelation(EntityRelation entityRelation) {
        this.from = entityRelation.getFrom();
        this.to = entityRelation.getTo();
        this.type = entityRelation.getType();
        this.typeGroup = entityRelation.getTypeGroup();
        this.additionalInfo = entityRelation.getAdditionalInfo();
    }

    public EntityId getFrom() {
        return from;
    }

    public void setFrom(EntityId from) {
        this.from = from;
    }

    public EntityId getTo() {
        return to;
    }

    public void setTo(EntityId to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RelationTypeGroup getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(RelationTypeGroup typeGroup) {
        this.typeGroup = typeGroup;
    }


    public JsonNode getAdditionalInfo() {
        return SearchTextBasedWithAdditionalInfo.getJson(() -> additionalInfo, () -> additionalInfoBytes);
    }

    public void setAdditionalInfo(JsonNode addInfo) {
        SearchTextBasedWithAdditionalInfo.setJson(addInfo, json -> this.additionalInfo = json, bytes -> this.additionalInfoBytes = bytes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityRelation that = (EntityRelation) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return typeGroup == that.typeGroup;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (typeGroup != null ? typeGroup.hashCode() : 0);
        return result;
    }
}
