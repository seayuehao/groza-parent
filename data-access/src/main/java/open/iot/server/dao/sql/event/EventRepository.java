package open.iot.server.dao.sql.event;

import open.iot.server.common.data.EntityType;
import open.iot.server.dao.model.sql.EventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EventRepository extends CrudRepository<EventEntity, String>, JpaSpecificationExecutor<EventEntity> {

    EventEntity findByTenantIdAndEntityTypeAndEntityIdAndEventTypeAndEventUid(String tenantId,
                                                                              EntityType entityType,
                                                                              String entityId,
                                                                              String eventType,
                                                                              String eventUid);

    EventEntity findByTenantIdAndEntityTypeAndEntityId(String tenantId,
                                                       EntityType entityType,
                                                       String entityId);

    @Query("SELECT e FROM EventEntity e WHERE e.tenantId = :tenantId AND e.entityType = :entityType " +
            "AND e.entityId = :entityId AND e.eventType = :eventType ORDER BY e.eventType DESC, e.id DESC")
    List<EventEntity> findLatestByTenantIdAndEntityTypeAndEntityIdAndEventType(
            @Param("tenantId") String tenantId,
            @Param("entityType") EntityType entityType,
            @Param("entityId") String entityId,
            @Param("eventType") String eventType,
            Pageable pageable);
}
