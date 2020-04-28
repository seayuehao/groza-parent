package open.iot.server.dao.alarm;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.alarm.Alarm;
import open.iot.server.common.data.alarm.AlarmInfo;
import open.iot.server.common.data.alarm.AlarmQuery;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.dao.Dao;

import java.util.List;
import java.util.UUID;

/**
 * @author james mu
 * @date 19-1-3 下午5:15
 */
public interface AlarmDao extends Dao<Alarm> {

    ListenableFuture<Alarm> findLatestByOriginatorAndType(TenantId tenantId, EntityId originator, String type);

    ListenableFuture<Alarm> findAlarmByIdAsync(UUID key);

    Alarm save(Alarm alarm);

    ListenableFuture<List<AlarmInfo>> findAlarms(AlarmQuery query);
}
