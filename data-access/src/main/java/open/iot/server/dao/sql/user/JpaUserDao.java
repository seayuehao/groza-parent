package open.iot.server.dao.sql.user;

import open.iot.server.common.data.User;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.common.data.security.Authority;
import open.iot.server.dao.DaoUtil;
import open.iot.server.dao.model.sql.UserEntity;
import open.iot.server.dao.sql.JpaAbstractSearchTextDao;
import open.iot.server.dao.user.UserDao;
import open.iot.server.dao.util.SqlDao;
import open.iot.server.dao.model.ModelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static open.iot.server.common.data.UUIDConverter.fromTimeUUID;

/**
 * @author james mu
 * @date 19-2-20 下午4:06
 * @description
 */
@SqlDao
@Component
public class JpaUserDao extends JpaAbstractSearchTextDao<UserEntity, User> implements UserDao {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }

    @Override
    protected CrudRepository<UserEntity, String> getCrudRepository() {
        return userRepository;
    }

    @Override
    public User findByEmail(String email) {
        return DaoUtil.getData(userRepository.findByEmail(email));
    }

    @Override
    public List<User> findTenantAdmins(UUID tenantId, TextPageLink pageLink) {
        return DaoUtil.convertDataList(
                userRepository
                        .findUsersByAuthority(
                                fromTimeUUID(tenantId),
                                ModelConstants.NULL_UUID_STR,
                                pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                                Objects.toString(pageLink.getTextSearch(), ""),
                                Authority.TENANT_ADMIN,
                                PageRequest.of(0, pageLink.getLimit())));
    }

    @Override
    public List<User> findCustomerUsers(UUID tenantId, UUID customerId, TextPageLink pageLink) {
        return DaoUtil.convertDataList(
                userRepository
                        .findUsersByAuthority(
                                fromTimeUUID(tenantId),
                                fromTimeUUID(customerId),
                                pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                                Objects.toString(pageLink.getTextSearch(), ""),
                                Authority.CUSTOMER_USER,
                            PageRequest.of(0, pageLink.getLimit())));
    }
}
