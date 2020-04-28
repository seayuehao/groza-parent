package open.iot.server.dao.tenant;

import open.iot.server.common.data.Tenant;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.dao.Dao;

import java.util.List;

/**
 * @author james mu
 * @date 19-1-3 下午12:04
 */
public interface TenantDao extends Dao<Tenant> {
    /**
     * Save or update tenant object
     *
     * @param tenant the tenant object
     * @return saved tenant object
     */
    Tenant save(Tenant tenant);

    /**
     * Find tenants by region
     * @param region
     * @return
     */
    List<Tenant> findTenantsByRegion(String region, TextPageLink pageLink);
}
