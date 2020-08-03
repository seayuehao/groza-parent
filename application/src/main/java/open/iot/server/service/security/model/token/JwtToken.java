package open.iot.server.service.security.model.token;

import java.io.Serializable;


public interface JwtToken extends Serializable {
    String getToken();
}
