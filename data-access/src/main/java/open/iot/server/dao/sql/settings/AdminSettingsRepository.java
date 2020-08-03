package open.iot.server.dao.sql.settings;

import open.iot.server.dao.model.sql.AdminSettingsEntity;
import org.springframework.data.repository.CrudRepository;


public interface AdminSettingsRepository extends CrudRepository<AdminSettingsEntity, String> {

    AdminSettingsEntity findByKey(String key);
}
