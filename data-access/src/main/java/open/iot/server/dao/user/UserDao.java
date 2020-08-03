package open.iot.server.dao.user;

import open.iot.server.common.data.User;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.dao.Dao;

import java.util.List;
import java.util.UUID;


public interface UserDao extends Dao<User> {
    /**
     * Save or update user object
     *
     * @param user the user object
     * @return saved user entity
     */
    User save(User user);

    /**
     * Find user by email
     *
     * @param email the email
     * @return the user entity
     */
    User findByEmail(String email);

    /**
     * Find tenant admin users by tenantId and page link.
     *
     * @param tenantId the tenantId
     * @param pageLink the page link
     * @return the list of user user entities
     */
    List<User> findTenantAdmins(UUID tenantId, TextPageLink pageLink);

    /**
     * Find customer users by tenantId, customerId and page link.
     *
     * @param tenantId   the tenantId
     * @param customerId the customerId
     * @param pageLink   the page link
     * @return the list of user entities
     */
    List<User> findCustomerUsers(UUID tenantId, UUID customerId, TextPageLink pageLink);
}
