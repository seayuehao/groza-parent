package open.iot.server.dao.settings;

import open.iot.server.common.data.AdminSettings;
import open.iot.server.common.data.id.AdminSettingsId;


public interface AdminSettingsService {

    AdminSettings findAdminSettingsById(AdminSettingsId adminSettingsId);

    AdminSettings findAdminSettingsByKey(String key);

    AdminSettings saveAdminSettings(AdminSettings adminSettings);
}
