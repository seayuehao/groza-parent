package open.iot.server.service.security.auth;

import open.iot.server.service.security.model.SecurityUser;
import open.iot.server.service.security.model.token.RawAccessJwtToken;

/**
 * @author james mu
 * @date 19-3-20 下午2:35
 * @description
 */
public class RefreshAuthenticationToken extends AbstractJwtAuthenticationToken {

    private static final long serialVersionUID = -1311042791508924523L;

    public RefreshAuthenticationToken(RawAccessJwtToken unsafeToken) {
        super(unsafeToken);
    }

    public RefreshAuthenticationToken(SecurityUser securityUser) {
        super(securityUser);
    }
}
