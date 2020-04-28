package open.iot.server.dao.settings;

import open.iot.server.common.data.AdminSettings;
import open.iot.server.common.data.id.AdminSettingsId;

/**
 * @author james mu
 * @date 19-2-28 下午1:38
 * @description
 */
public interface AdminSettingsService {

    AdminSettings findAdminSettingsById(AdminSettingsId adminSettingsId);

    AdminSettings findAdminSettingsByKey(String key);

    AdminSettings saveAdminSettings(AdminSettings adminSettings);
}
