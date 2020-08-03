package open.iot.server.dao.audit.sink;

import open.iot.server.common.data.audit.AuditLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(prefix = "audit_log.sink", value = "type", havingValue = "none")
public class DummyAuditLogSink implements AuditLogSink{

    @Override
    public void logAction(AuditLog auditLogEntry) {
    }
}
