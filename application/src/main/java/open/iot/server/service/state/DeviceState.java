package open.iot.server.service.state;

public class DeviceState {

    private boolean active;
    private long lastConnectTime;
    private long lastActivityTime;
    private long lastDisconnectTime;
    private long lastInactivityAlarmTime;
    private long inactivityTimeout;

    public DeviceState() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getLastConnectTime() {
        return lastConnectTime;
    }

    public void setLastConnectTime(long lastConnectTime) {
        this.lastConnectTime = lastConnectTime;
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    public void setLastActivityTime(long lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
    }

    public long getLastDisconnectTime() {
        return lastDisconnectTime;
    }

    public void setLastDisconnectTime(long lastDisconnectTime) {
        this.lastDisconnectTime = lastDisconnectTime;
    }

    public long getLastInactivityAlarmTime() {
        return lastInactivityAlarmTime;
    }

    public void setLastInactivityAlarmTime(long lastInactivityAlarmTime) {
        this.lastInactivityAlarmTime = lastInactivityAlarmTime;
    }

    public long getInactivityTimeout() {
        return inactivityTimeout;
    }

    public void setInactivityTimeout(long inactivityTimeout) {
        this.inactivityTimeout = inactivityTimeout;
    }
}
