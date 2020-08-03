package open.iot.server.dao.sql.rule;

import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.common.data.rule.RuleChain;
import open.iot.server.dao.DaoUtil;
import open.iot.server.dao.model.ModelConstants;
import open.iot.server.dao.model.sql.RuleChainEntity;
import open.iot.server.dao.rule.RuleChainDao;
import open.iot.server.dao.sql.JpaAbstractSearchTextDao;
import open.iot.server.dao.util.SqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Component
@SqlDao
public class JpaRuleChainDao extends JpaAbstractSearchTextDao<RuleChainEntity, RuleChain> implements RuleChainDao {

    @Autowired
    private RuleChainRepository ruleChainRepository;

    @Override
    public List<RuleChain> findRuleChainsByTenantId(UUID tenantId, TextPageLink pageLink) {
        return DaoUtil.convertDataList(ruleChainRepository
                .findByTenantId(
                        UUIDConverter.fromTimeUUID(tenantId),
                        Objects.toString(pageLink.getTextSearch(), ""),
                        pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : UUIDConverter.fromTimeUUID(pageLink.getIdOffset()),
                        PageRequest.of(0, pageLink.getLimit())));
    }

    @Override
    protected Class<RuleChainEntity> getEntityClass() {
        return RuleChainEntity.class;
    }

    @Override
    protected CrudRepository<RuleChainEntity, String> getCrudRepository() {
        return ruleChainRepository;
    }
}
