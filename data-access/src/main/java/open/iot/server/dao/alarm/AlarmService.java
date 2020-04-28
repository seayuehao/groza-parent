package open.iot.server.dao.alarm;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.alarm.Alarm;
import open.iot.server.common.data.alarm.AlarmId;
import open.iot.server.common.data.alarm.AlarmInfo;
import open.iot.server.common.data.alarm.AlarmQuery;
import open.iot.server.common.data.alarm.AlarmSearchStatus;
import open.iot.server.common.data.alarm.AlarmSeverity;
import open.iot.server.common.data.alarm.AlarmStatus;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.page.TimePageData;

/**
 * @author james mu
 * @date 19-1-3 下午5:23
 */
public interface AlarmService {

    Alarm createOrUpdateAlarm(Alarm alarm);

    ListenableFuture<Boolean> ackAlarm(AlarmId alarmId, long ackTs);

    ListenableFuture<Boolean> clearAlarm(AlarmId alarmId, JsonNode details, long ackTs);

    ListenableFuture<Alarm> findAlarmByIdAsync(AlarmId alarmId);

    ListenableFuture<AlarmInfo> findAlarmInfoByIdAsync(AlarmId alarmId);

    ListenableFuture<TimePageData<AlarmInfo>> findAlarms(AlarmQuery query);

    AlarmSeverity findHighestAlarmSeverity(EntityId entityId, AlarmSearchStatus alarmSearchStatus,
                                           AlarmStatus alarmStatus);

    ListenableFuture<Alarm> findLatestByOriginatorAndType(TenantId tenantId, EntityId originator, String type);
}
