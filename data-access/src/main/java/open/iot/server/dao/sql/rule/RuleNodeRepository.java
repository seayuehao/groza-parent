package open.iot.server.dao.sql.rule;

import open.iot.server.dao.model.sql.RuleNodeEntity;
import open.iot.server.dao.util.SqlDao;
import org.springframework.data.repository.CrudRepository;

@SqlDao
public interface RuleNodeRepository extends CrudRepository<RuleNodeEntity, String> {
}
