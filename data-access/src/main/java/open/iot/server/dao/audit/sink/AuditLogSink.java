package open.iot.server.dao.audit.sink;

import open.iot.server.common.data.audit.AuditLog;


public interface AuditLogSink {

    void logAction(AuditLog auditLogEntry);
}
