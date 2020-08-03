package open.iot.server.dao.audit;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.audit.AuditLog;
import open.iot.server.common.data.id.CustomerId;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.UserId;
import open.iot.server.common.data.page.TimePageLink;

import java.util.List;
import java.util.UUID;


public interface AuditLogDao {

    ListenableFuture<Void> saveByTenantId(AuditLog auditLog);

    ListenableFuture<Void> saveByTenantIdAndEntityId(AuditLog auditLog);

    ListenableFuture<Void> saveByTenantIdAndCustomerId(AuditLog auditLog);

    ListenableFuture<Void> saveByTenantIdAndUserId(AuditLog auditLog);

    ListenableFuture<Void> savePartitionsByTenantId(AuditLog auditLog);

    List<AuditLog> findAuditLogsByTenantIdAndEntityId(UUID tenantId, EntityId entityId, TimePageLink pageLink);

    List<AuditLog> findAuditLogsByTenantIdAndCustomerId(UUID tenantId, CustomerId customerId, TimePageLink pageLink);

    List<AuditLog> findAuditLogsByTenantIdAndUserId(UUID tenantId, UserId userId, TimePageLink pageLink);

    List<AuditLog> findAuditLogsByTenantId(UUID tenantId, TimePageLink pageLink);
}
