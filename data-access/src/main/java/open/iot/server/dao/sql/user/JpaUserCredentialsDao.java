package open.iot.server.dao.sql.user;

import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.security.UserCredentials;
import open.iot.server.dao.DaoUtil;
import open.iot.server.dao.model.sql.UserCredentialsEntity;
import open.iot.server.dao.sql.JpaAbstractDao;
import open.iot.server.dao.user.UserCredentialsDao;
import open.iot.server.dao.util.SqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@SqlDao
public class JpaUserCredentialsDao extends JpaAbstractDao<UserCredentialsEntity, UserCredentials> implements UserCredentialsDao {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Override
    protected Class<UserCredentialsEntity> getEntityClass() {
        return UserCredentialsEntity.class;
    }

    @Override
    protected CrudRepository<UserCredentialsEntity, String> getCrudRepository() {
        return userCredentialsRepository;
    }

    @Override
    public UserCredentials findByUserId(UUID userId) {
        return DaoUtil.getData(userCredentialsRepository.findByUserId(UUIDConverter.fromTimeUUID(userId)));
    }

    @Override
    public UserCredentials findByActivateToken(String activateToken) {
        return DaoUtil.getData(userCredentialsRepository.findByActivateToken(activateToken));
    }

    @Override
    public UserCredentials findByResetToken(String resetToken) {
        return DaoUtil.getData(userCredentialsRepository.findByResetToken(resetToken));
    }
}
