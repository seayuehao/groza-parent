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
import open.iot.server.common.data.EntityType;
import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.alarm.Alarm;
import open.iot.server.common.data.alarm.AlarmId;
import open.iot.server.common.data.alarm.AlarmSeverity;
import open.iot.server.common.data.alarm.AlarmStatus;
import open.iot.server.common.data.id.EntityIdFactory;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.dao.model.BaseEntity;
import open.iot.server.dao.model.BaseSqlEntity;
import open.iot.server.dao.model.ModelConstants;
import open.iot.server.dao.util.mapping.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = ModelConstants.ALARM_COLUMN_FAMILY_NAME)
public final class AlarmEntity extends BaseSqlEntity<Alarm> implements BaseEntity<Alarm> {

    @Column(name = ModelConstants.ALARM_TENANT_ID_PROPERTY)
    private String tenantId;

    @Column(name = ModelConstants.ALARM_ORIGINATOR_ID_PROPERTY)
    private String originatorId;

    @Column(name = ModelConstants.ALARM_ORIGINATOR_TYPE_PROPERTY)
    private EntityType originatorType;

    @Column(name = ModelConstants.ALARM_TYPE_PROPERTY)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = ModelConstants.ALARM_SEVERITY_PROPERTY)
    private AlarmSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = ModelConstants.ALARM_STATUS_PROPERTY)
    private AlarmStatus status;

    @Column(name = ModelConstants.ALARM_START_TS_PROPERTY)
    private Long startTs;

    @Column(name = ModelConstants.ALARM_END_TS_PROPERTY)
    private Long endTs;

    @Column(name = ModelConstants.ALARM_ACK_TS_PROPERTY)
    private Long ackTs;

    @Column(name = ModelConstants.ALARM_CLEAR_TS_PROPERTY)
    private Long clearTs;

    @Type(type = "json")
    @Column(name = ModelConstants.ASSET_ADDITIONAL_INFO_PROPERTY)
    private JsonNode details;

    @Column(name = ModelConstants.ALARM_PROPAGATE_PROPERTY)
    private Boolean propagate;

    public AlarmEntity() {
        super();
    }

    public AlarmEntity(Alarm alarm) {
        if (alarm.getId() != null) {
            this.setId(alarm.getId().getId());
        }
        if (alarm.getTenantId() != null) {
            this.tenantId = UUIDConverter.fromTimeUUID(alarm.getTenantId().getId());
        }
        this.type = alarm.getType();
        this.originatorId = UUIDConverter.fromTimeUUID(alarm.getOriginator().getId());
        this.originatorType = alarm.getOriginator().getEntityType();
        this.type = alarm.getType();
        this.severity = alarm.getSeverity();
        this.status = alarm.getStatus();
        this.propagate = alarm.isPropagate();
        this.startTs = alarm.getStartTs();
        this.endTs = alarm.getEndTs();
        this.ackTs = alarm.getAckTs();
        this.clearTs = alarm.getClearTs();
        this.details = alarm.getDetails();
    }

    @Override
    public Alarm toData() {
        Alarm alarm = new Alarm(new AlarmId(UUIDConverter.fromString(id)));
        alarm.setCreatedTime(UUIDs.unixTimestamp(UUIDConverter.fromString(id)));
        if (tenantId != null) {
            alarm.setTenantId(new TenantId(UUIDConverter.fromString(tenantId)));
        }
        alarm.setOriginator(EntityIdFactory.getByTypeAndUuid(originatorType, UUIDConverter.fromString(originatorId)));
        alarm.setType(type);
        alarm.setSeverity(severity);
        alarm.setStatus(status);
        alarm.setPropagate(propagate);
        alarm.setStartTs(startTs);
        alarm.setEndTs(endTs);
        alarm.setAckTs(ackTs);
        alarm.setClearTs(clearTs);
        alarm.setDetails(details);
        return alarm;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getOriginatorId() {
        return originatorId;
    }

    public void setOriginatorId(String originatorId) {
        this.originatorId = originatorId;
    }

    public EntityType getOriginatorType() {
        return originatorType;
    }

    public void setOriginatorType(EntityType originatorType) {
        this.originatorType = originatorType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AlarmSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AlarmSeverity severity) {
        this.severity = severity;
    }

    public AlarmStatus getStatus() {
        return status;
    }

    public void setStatus(AlarmStatus status) {
        this.status = status;
    }

    public Long getStartTs() {
        return startTs;
    }

    public void setStartTs(Long startTs) {
        this.startTs = startTs;
    }

    public Long getEndTs() {
        return endTs;
    }

    public void setEndTs(Long endTs) {
        this.endTs = endTs;
    }

    public Long getAckTs() {
        return ackTs;
    }

    public void setAckTs(Long ackTs) {
        this.ackTs = ackTs;
    }

    public Long getClearTs() {
        return clearTs;
    }

    public void setClearTs(Long clearTs) {
        this.clearTs = clearTs;
    }

    public JsonNode getDetails() {
        return details;
    }

    public void setDetails(JsonNode details) {
        this.details = details;
    }

    public Boolean getPropagate() {
        return propagate;
    }

    public void setPropagate(Boolean propagate) {
        this.propagate = propagate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AlarmEntity that = (AlarmEntity) o;
        return Objects.equals(tenantId, that.tenantId) &&
                Objects.equals(originatorId, that.originatorId) &&
                originatorType == that.originatorType &&
                Objects.equals(type, that.type) &&
                severity == that.severity &&
                status == that.status &&
                Objects.equals(startTs, that.startTs) &&
                Objects.equals(endTs, that.endTs) &&
                Objects.equals(ackTs, that.ackTs) &&
                Objects.equals(clearTs, that.clearTs) &&
                Objects.equals(details, that.details) &&
                Objects.equals(propagate, that.propagate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tenantId, originatorId, originatorType, type, severity, status, startTs, endTs, ackTs, clearTs, details, propagate);
    }
}