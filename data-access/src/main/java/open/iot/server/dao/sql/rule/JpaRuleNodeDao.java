package open.iot.server.dao.sql.rule;

import open.iot.server.common.data.rule.RuleNode;
import open.iot.server.dao.model.sql.RuleNodeEntity;
import open.iot.server.dao.rule.RuleNodeDao;
import open.iot.server.dao.sql.JpaAbstractSearchTextDao;
import open.iot.server.dao.util.SqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


@Component
@SqlDao
public class JpaRuleNodeDao extends JpaAbstractSearchTextDao<RuleNodeEntity, RuleNode> implements RuleNodeDao {

    @Autowired
    private RuleNodeRepository ruleNodeRepository;

    @Override
    protected Class getEntityClass() {
        return RuleNodeEntity.class;
    }

    @Override
    protected CrudRepository getCrudRepository() {
        return ruleNodeRepository;
    }
}
