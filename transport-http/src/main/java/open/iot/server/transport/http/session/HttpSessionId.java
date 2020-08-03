package open.iot.server.transport.http.session;

import open.iot.server.common.data.id.SessionId;

import java.util.UUID;


public class HttpSessionId implements SessionId {

    private final UUID id;

    public HttpSessionId(){
        this.id = UUID.randomUUID();
    }

    @Override
    public String toUidStr() {
        return id.toString();
    }
}
