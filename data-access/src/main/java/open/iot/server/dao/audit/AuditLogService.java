package open.iot.server.dao.audit;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.BaseData;
import open.iot.server.common.data.HasName;
import open.iot.server.common.data.audit.ActionType;
import open.iot.server.common.data.audit.AuditLog;
import open.iot.server.common.data.id.CustomerId;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.id.UUIDBased;
import open.iot.server.common.data.id.UserId;
import open.iot.server.common.data.page.TimePageData;
import open.iot.server.common.data.page.TimePageLink;

import java.util.List;

public interface AuditLogService {

    TimePageData<AuditLog> findAuditLogsByTenantIdAndCustomerId(TenantId tenantId, CustomerId customerId, TimePageLink pageLink);

    TimePageData<AuditLog> findAuditLogsByTenantIdAndUserId(TenantId tenantId, UserId userId, TimePageLink pageLink);

    TimePageData<AuditLog> findAuditLogsByTenantIdAndEntityId(TenantId tenantId, EntityId entityId, TimePageLink pageLink);

    TimePageData<AuditLog> findAuditLogsByTenantId(TenantId tenantId, TimePageLink pageLink);

    <E extends BaseData<I> & HasName,
            I extends UUIDBased & EntityId> ListenableFuture<List<Void>> logEntityAction(TenantId tenantId,
                                                                                         CustomerId customerId,
                                                                                         UserId userId,
                                                                                         String userName,
                                                                                         I entityId,
                                                                                         E entity,
                                                                                         ActionType actionType,
                                                                                         Exception e, Object... additionalInfo);
}
