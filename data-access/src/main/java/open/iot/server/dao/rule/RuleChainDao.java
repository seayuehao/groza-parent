package open.iot.server.dao.rule;

import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.common.data.rule.RuleChain;
import open.iot.server.dao.Dao;

import java.util.List;
import java.util.UUID;


public interface RuleChainDao extends Dao<RuleChain> {

    /**
     * Find rule chains by tenantId and page link.
     *
     * @param tenantId the tenantId
     * @param pageLink the page link
     * @return the list of rule chain objects
     */
    List<RuleChain> findRuleChainsByTenantId(UUID tenantId, TextPageLink pageLink);
}
