package open.iot.server.dao.model.sql;

import open.iot.server.common.data.EntityType;
import open.iot.server.dao.model.ToData;
import lombok.Data;
import open.iot.server.common.data.kv.BasicTsKvEntry;
import open.iot.server.common.data.kv.BooleanDataEntry;
import open.iot.server.common.data.kv.DoubleDataEntry;
import open.iot.server.common.data.kv.KvEntry;
import open.iot.server.common.data.kv.LongDataEntry;
import open.iot.server.common.data.kv.StringDataEntry;
import open.iot.server.common.data.kv.TsKvEntry;

import javax.persistence.*;

import static open.iot.server.dao.model.ModelConstants.*;

/**
 * @author james mu
 * @date 19-1-30 下午4:14
 * @description
 */
@Data
@Entity
@Table(name = "ts_kv_latest")
@IdClass(TsKvLatestCompositeKey.class)
public final class TsKvLatestEntity implements ToData<TsKvEntry> {


    //TODO: reafctor this and TsKvEntity to avoid code duplicates
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = ENTITY_TYPE_COLUMN)
    private EntityType entityType;

    @Id
    @Column(name = ENTITY_ID_COLUMN)
    private String entityId;

    @Id
    @Column(name = KEY_COLUMN)
    private String key;

    @Column(name = TS_COLUMN)
    private long ts;

    @Column(name = BOOLEAN_VALUE_COLUMN)
    private Boolean booleanValue;

    @Column(name = STRING_VALUE_COLUMN)
    private String strValue;

    @Column(name = LONG_VALUE_COLUMN)
    private Long longValue;

    @Column(name = DOUBLE_VALUE_COLUMN)
    private Double doubleValue;

    @Override
    public TsKvEntry toData() {
        KvEntry kvEntry = null;
        if (strValue != null) {
            kvEntry = new StringDataEntry(key, strValue);
        } else if (longValue != null) {
            kvEntry = new LongDataEntry(key, longValue);
        } else if (doubleValue != null) {
            kvEntry = new DoubleDataEntry(key, doubleValue);
        } else if (booleanValue != null) {
            kvEntry = new BooleanDataEntry(key, booleanValue);
        }
        return new BasicTsKvEntry(ts, kvEntry);
    }
}
