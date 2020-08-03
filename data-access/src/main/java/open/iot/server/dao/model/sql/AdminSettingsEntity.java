/**
 * Copyright © 2016-2018 The Thingsboard Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package open.iot.server.dao.model.sql;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import open.iot.server.common.data.AdminSettings;
import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.id.AdminSettingsId;
import open.iot.server.dao.model.BaseEntity;
import open.iot.server.dao.model.BaseSqlEntity;
import open.iot.server.dao.model.ModelConstants;
import open.iot.server.dao.util.mapping.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = ModelConstants.ADMIN_SETTINGS_COLUMN_FAMILY_NAME)
public final class AdminSettingsEntity extends BaseSqlEntity<AdminSettings> implements BaseEntity<AdminSettings> {

    @Column(name = ModelConstants.ADMIN_SETTINGS_KEY_PROPERTY)
    private String key;

    @Type(type = "json")
    @Column(name = ModelConstants.ADMIN_SETTINGS_JSON_VALUE_PROPERTY)
    private JsonNode jsonValue;

    public AdminSettingsEntity() {
        super();
    }

    public AdminSettingsEntity(AdminSettings adminSettings) {
        if (adminSettings.getId() != null) {
            this.setId(adminSettings.getId().getId());
        }
        this.key = adminSettings.getKey();
        this.jsonValue = adminSettings.getJsonValue();
    }

    @Override
    public AdminSettings toData() {
        AdminSettings adminSettings = new AdminSettings(new AdminSettingsId(UUIDConverter.fromString(id)));
        adminSettings.setCreatedTime(UUIDs.unixTimestamp(UUIDConverter.fromString(id)));
        adminSettings.setKey(key);
        adminSettings.setJsonValue(jsonValue);
        return adminSettings;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JsonNode getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(JsonNode jsonValue) {
        this.jsonValue = jsonValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AdminSettingsEntity that = (AdminSettingsEntity) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(jsonValue, that.jsonValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, jsonValue);
    }
}
