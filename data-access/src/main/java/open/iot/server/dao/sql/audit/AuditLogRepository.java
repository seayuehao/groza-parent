package open.iot.server.dao.sql.audit;

import open.iot.server.dao.model.sql.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface AuditLogRepository extends CrudRepository<AuditLogEntity, String>, JpaSpecificationExecutor<AuditLogEntity> {

}
