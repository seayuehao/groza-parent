package open.iot.server.dao.sql.alarm;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.EntityType;
import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.alarm.Alarm;
import open.iot.server.common.data.alarm.AlarmInfo;
import open.iot.server.common.data.alarm.AlarmQuery;
import open.iot.server.common.data.alarm.AlarmSearchStatus;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.relation.EntityRelation;
import open.iot.server.common.data.relation.RelationTypeGroup;
import open.iot.server.dao.DaoUtil;
import open.iot.server.dao.alarm.AlarmDao;
import open.iot.server.dao.alarm.BaseAlarmService;
import open.iot.server.dao.model.sql.AlarmEntity;
import open.iot.server.dao.relation.RelationDao;
import open.iot.server.dao.sql.JpaAbstractDao;
import open.iot.server.dao.util.SqlDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;


@SqlDao
@Component
public class JpaAlarmDao extends JpaAbstractDao<AlarmEntity, Alarm> implements AlarmDao {

    private static final Logger log = LoggerFactory.getLogger("JpaAlarmDao");

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private RelationDao relationDao;

    @Override
    protected Class<AlarmEntity> getEntityClass() {
        return AlarmEntity.class;
    }

    @Override
    protected CrudRepository<AlarmEntity, String> getCrudRepository() {
        return alarmRepository;
    }

    @Override
    public ListenableFuture<Alarm> findLatestByOriginatorAndType(TenantId tenantId, EntityId originator, String type) {
        return service.submit(() -> {
            List<AlarmEntity> latest = alarmRepository.findLatestByOriginatorAndType(
                    UUIDConverter.fromTimeUUID(tenantId.getId()),
                    UUIDConverter.fromTimeUUID(originator.getId()),
                    originator.getEntityType(),
                    type,
                    PageRequest.of(0, 1)
            );
            return latest.isEmpty() ? null : DaoUtil.getData(latest.get(0));
        });
    }

    @Override
    public ListenableFuture<Alarm> findAlarmByIdAsync(UUID key) {
        return findByIdAsync(key);
    }

    @Override
    public ListenableFuture<List<AlarmInfo>> findAlarms(AlarmQuery query) {
        log.trace("Try to find alarms by entity [{}], status [{}] and pageLink [{}]", query.getAffectedEntityId(), query.getStatus(), query.getPageLink());
        EntityId affectedEntity = query.getAffectedEntityId();
        String searchStatusName;
        if (query.getSearchStatus() == null && query.getStatus() == null) {
            searchStatusName = AlarmSearchStatus.ANY.name();
        } else if (query.getSearchStatus() != null) {
            searchStatusName = query.getSearchStatus().name();
        } else {
            searchStatusName = query.getStatus().name();
        }
        String relationType = BaseAlarmService.ALARM_RELATION_PREFIX + searchStatusName;
        ListenableFuture<List<EntityRelation>> relations = relationDao.findRelations(affectedEntity, relationType, RelationTypeGroup.ALARM, EntityType.ALARM, query.getPageLink());
        return Futures.transformAsync(relations, input -> {
            List<ListenableFuture<AlarmInfo>> alarmFutures = new ArrayList<>(input.size());
            for (EntityRelation relation : input) {
                alarmFutures.add(Futures.transform(
                        findAlarmByIdAsync(relation.getTo().getId()),
                        AlarmInfo::new, Executors.newSingleThreadExecutor()));
            }
            return Futures.successfulAsList(alarmFutures);
        }, Executors.newSingleThreadExecutor());
    }
}
