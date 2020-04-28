package open.iot.server.dao.model.sql;

import open.iot.server.common.data.EntityType;
import open.iot.server.dao.model.ToData;
import lombok.Data;
import open.iot.server.common.data.kv.AttributeKvEntry;
import open.iot.server.common.data.kv.BaseAttributeKvEntry;
import open.iot.server.common.data.kv.BooleanDataEntry;
import open.iot.server.common.data.kv.DoubleDataEntry;
import open.iot.server.common.data.kv.KvEntry;
import open.iot.server.common.data.kv.LongDataEntry;
import open.iot.server.common.data.kv.StringDataEntry;

import javax.persistence.*;
import java.io.Serializable;

import static open.iot.server.dao.model.ModelConstants.*;

/**
 * @author james mu
 * @date 18-12-13 上午10:10
 */
@Data
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
        if (strValue != null){
            kvEntry = new StringDataEntry(attributeKey,strValue);
        }else if (booleanValue != null){
            kvEntry = new BooleanDataEntry(attributeKey,booleanValue);
        }else if (doubleValue != null){
            kvEntry = new DoubleDataEntry(attributeKey,doubleValue);
        }else if (longValue != null){
            kvEntry = new LongDataEntry(attributeKey,longValue);
        }
        return new BaseAttributeKvEntry(kvEntry,lastUpdateTs);
    }
}
