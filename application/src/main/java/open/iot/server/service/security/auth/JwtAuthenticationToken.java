package open.iot.server.service.security.auth;

import open.iot.server.service.security.model.SecurityUser;
import open.iot.server.service.security.model.token.RawAccessJwtToken;


public class JwtAuthenticationToken extends AbstractJwtAuthenticationToken {

    private static final long serialVersionUID = -8487219769037942225L;

    public JwtAuthenticationToken(RawAccessJwtToken unsafeToken) {
        super(unsafeToken);
    }

    public JwtAuthenticationToken(SecurityUser securityUser) {
        super(securityUser);
    }
}
