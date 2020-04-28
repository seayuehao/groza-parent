package open.iot.server.dao.sql.timeseries;

import open.iot.server.common.data.EntityType;
import open.iot.server.dao.model.sql.TsKvLatestCompositeKey;
import open.iot.server.dao.model.sql.TsKvLatestEntity;
import open.iot.server.dao.util.SqlDao;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author james mu
 * @date 19-1-30 下午4:43
 * @description
 */
@SqlDao
public interface TsKvLatestRepository extends CrudRepository<TsKvLatestEntity, TsKvLatestCompositeKey> {

    List<TsKvLatestEntity> findAllByEntityTypeAndEntityId(EntityType entityType, String entityId);
}
