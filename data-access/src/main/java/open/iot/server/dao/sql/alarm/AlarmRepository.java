package open.iot.server.dao.sql.alarm;

import open.iot.server.common.data.EntityType;
import open.iot.server.dao.model.sql.AlarmEntity;
import open.iot.server.dao.util.SqlDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author james mu
 * @date 19-2-21 上午9:41
 * @description
 */
@SqlDao
public interface AlarmRepository extends CrudRepository<AlarmEntity, String> {

    @Query("SELECT a FROM AlarmEntity a WHERE a.tenantId = :tenantId AND a.originatorId = :originatorId " +
            "AND a.originatorType = :entityType AND a.type = :alarmType ORDER BY a.type ASC, a.id DESC")
    List<AlarmEntity> findLatestByOriginatorAndType(@Param("tenantId") String tenantId,
                                                    @Param("originatorId") String originatorId,
                                                    @Param("entityType") EntityType entityType,
                                                    @Param("alarmType") String alarmType,
                                                    Pageable pageable);
}
