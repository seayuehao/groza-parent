package open.iot.server.dao.settings;

import open.iot.server.common.data.AdminSettings;
import open.iot.server.common.data.id.AdminSettingsId;
import open.iot.server.dao.exception.DataValidationException;
import open.iot.server.dao.service.DataValidator;
import open.iot.server.dao.service.Validator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminSettingsServiceImpl implements AdminSettingsService {

    private static final Logger log = LoggerFactory.getLogger("AdminSettingsService");

    @Autowired
    private AdminSettingsDao adminSettingsDao;

    @Override
    public AdminSettings findAdminSettingsById(AdminSettingsId adminSettingsId) {
        log.trace("Executing findAdminSettingsById [{}]", adminSettingsId);
        Validator.validateId(adminSettingsId, "Incorrect adminSettingsId " + adminSettingsId);
        return adminSettingsDao.findById(adminSettingsId.getId());
    }

    @Override
    public AdminSettings findAdminSettingsByKey(String key) {
        log.trace("Executing findAdminSettingsByKey [{}]", key);
        Validator.validateString(key, "Incorrect key " + key);
        return adminSettingsDao.findByKey(key);
    }

    @Override
    public AdminSettings saveAdminSettings(AdminSettings adminSettings) {
        log.trace("Executing saveAdminSettings [{}]", adminSettings);
        adminSettingsValidator.validate(adminSettings);
        return adminSettingsDao.save(adminSettings);
    }

    private DataValidator<AdminSettings> adminSettingsValidator = new DataValidator<AdminSettings>() {

                @Override
                protected void validateCreate(AdminSettings adminSettings) {
                    AdminSettings existentAdminSettingsWithKey = findAdminSettingsByKey(adminSettings.getKey());
                    if (existentAdminSettingsWithKey != null) {
                        throw new DataValidationException("Admin settings with such name already exists!");
                    }
                }

                @Override
                protected void validateUpdate(AdminSettings adminSettings) {
                    AdminSettings existentAdminSettings = findAdminSettingsById(adminSettings.getId());
                    if (existentAdminSettings != null) {
                        if (!existentAdminSettings.getKey().equals(adminSettings.getKey())) {
                            throw new DataValidationException("Changing key of admin settings entry is prohibited!");
                        }
                        validateJsonStructure(existentAdminSettings.getJsonValue(), adminSettings.getJsonValue());
                    }
                }


                @Override
                protected void validateDataImpl(AdminSettings adminSettings) {
                    if (StringUtils.isEmpty(adminSettings.getKey())) {
                        throw new DataValidationException("Key should be specified!");
                    }
                    if (adminSettings.getJsonValue() == null) {
                        throw new DataValidationException("Json value should be specified!");
                    }
                }
            };
}
