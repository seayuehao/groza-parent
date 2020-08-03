package open.iot.server.dao.audit;


public enum  AuditLogLevelMask {

    OFF(false, false),
    W(true, false),
    RW(true, true);

    private final boolean write;
    private final boolean read;

    AuditLogLevelMask(boolean write, boolean read){
        this.write = write;
        this.read = read;
    }

    public boolean isWrite() {
        return write;
    }

    public boolean isRead() {
        return read;
    }
}
