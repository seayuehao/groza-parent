package open.iot.server.common.data.audit;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import open.iot.server.common.data.BaseData;
import open.iot.server.common.data.id.AuditLogId;
import open.iot.server.common.data.id.CustomerId;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.id.UserId;

/**
 * @author james mu
 * @date 19-1-22 下午3:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuditLog extends BaseData<AuditLogId> {

    private TenantId tenantId;
    private CustomerId customerId;
    private EntityId entityId;
    private String entityName;
    private UserId userId;
    private String userName;
    private ActionType actionType;
    private JsonNode actionData;
    private ActionStatus actionStatus;
    private String actionFailureDetails;

    public AuditLog() {
        super();
    }

    public AuditLog(AuditLogId id) {
        super(id);
    }

    public AuditLog(AuditLog auditLog) {
        super(auditLog);
        this.tenantId = auditLog.getTenantId();
        this.customerId = auditLog.getCustomerId();
        this.entityId = auditLog.getEntityId();
        this.entityName = auditLog.getEntityName();
        this.userId = auditLog.getUserId();
        this.userName = auditLog.getUserName();
        this.actionType = auditLog.getActionType();
        this.actionData = auditLog.getActionData();
        this.actionStatus = auditLog.getActionStatus();
        this.actionFailureDetails = auditLog.getActionFailureDetails();
    }
}
