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
import open.iot.server.common.data.audit.ActionStatus;
import open.iot.server.common.data.audit.ActionType;
import open.iot.server.common.data.audit.AuditLog;
import open.iot.server.common.data.id.AuditLogId;
import open.iot.server.common.data.id.CustomerId;
import open.iot.server.common.data.id.EntityIdFactory;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.id.UserId;
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

import static open.iot.server.dao.model.ModelConstants.AUDIT_LOG_ACTION_DATA_PROPERTY;
import static open.iot.server.dao.model.ModelConstants.AUDIT_LOG_ACTION_FAILURE_DETAILS_PROPERTY;
import static open.iot.server.dao.model.ModelConstants.AUDIT_LOG_ACTION_STATUS_PROPERTY;
import static open.iot.server.dao.model.ModelConstants.AUDIT_LOG_ACTION_TYPE_PROPERTY;
import static open.iot.server.dao.model.ModelConstants.AUDIT_LOG_CUSTOMER_ID_PROPERTY;
import static open.iot.server.dao.model.ModelConstants.AUDIT_LOG_ENTITY_ID_PROPERTY;
import static open.iot.server.dao.model.ModelConstants.AUDIT_LOG_ENTITY_NAME_PROPERTY;
import static open.iot.server.dao.model.ModelConstants.AUDIT_LOG_ENTITY_TYPE_PROPERTY;
import static open.iot.server.dao.model.ModelConstants.AUDIT_LOG_TENANT_ID_PROPERTY;
import static open.iot.server.dao.model.ModelConstants.AUDIT_LOG_USER_ID_PROPERTY;
import static open.iot.server.dao.model.ModelConstants.AUDIT_LOG_USER_NAME_PROPERTY;


@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = ModelConstants.AUDIT_LOG_COLUMN_FAMILY_NAME)
public class AuditLogEntity extends BaseSqlEntity<AuditLog> implements BaseEntity<AuditLog> {

    @Column(name = AUDIT_LOG_TENANT_ID_PROPERTY)
    private String tenantId;

    @Column(name = AUDIT_LOG_CUSTOMER_ID_PROPERTY)
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = AUDIT_LOG_ENTITY_TYPE_PROPERTY)
    private EntityType entityType;

    @Column(name = AUDIT_LOG_ENTITY_ID_PROPERTY)
    private String entityId;

    @Column(name = AUDIT_LOG_ENTITY_NAME_PROPERTY)
    private String entityName;

    @Column(name = AUDIT_LOG_USER_ID_PROPERTY)
    private String userId;

    @Column(name = AUDIT_LOG_USER_NAME_PROPERTY)
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(name = AUDIT_LOG_ACTION_TYPE_PROPERTY)
    private ActionType actionType;

    @Type(type = "json")
    @Column(name = AUDIT_LOG_ACTION_DATA_PROPERTY)
    private JsonNode actionData;

    @Enumerated(EnumType.STRING)
    @Column(name = AUDIT_LOG_ACTION_STATUS_PROPERTY)
    private ActionStatus actionStatus;

    @Column(name = AUDIT_LOG_ACTION_FAILURE_DETAILS_PROPERTY)
    private String actionFailureDetails;

    public AuditLogEntity() {
        super();
    }

    public AuditLogEntity(AuditLog auditLog) {
        if (auditLog.getId() != null) {
            this.setId(auditLog.getId().getId());
        }
        if (auditLog.getTenantId() != null) {
            this.tenantId = toString(auditLog.getTenantId().getId());
        }
        if (auditLog.getCustomerId() != null) {
            this.customerId = toString(auditLog.getCustomerId().getId());
        }
        if (auditLog.getEntityId() != null) {
            this.entityId = toString(auditLog.getEntityId().getId());
            this.entityType = auditLog.getEntityId().getEntityType();
        }
        if (auditLog.getUserId() != null) {
            this.userId = toString(auditLog.getUserId().getId());
        }
        this.entityName = auditLog.getEntityName();
        this.userName = auditLog.getUserName();
        this.actionType = auditLog.getActionType();
        this.actionData = auditLog.getActionData();
        this.actionStatus = auditLog.getActionStatus();
        this.actionFailureDetails = auditLog.getActionFailureDetails();
    }

    @Override
    public AuditLog toData() {
        AuditLog auditLog = new AuditLog(new AuditLogId(getId()));
        auditLog.setCreatedTime(UUIDs.unixTimestamp(getId()));
        if (tenantId != null) {
            auditLog.setTenantId(new TenantId(toUUID(tenantId)));
        }
        if (customerId != null) {
            auditLog.setCustomerId(new CustomerId(toUUID(customerId)));
        }
        if (entityId != null) {
            auditLog.setEntityId(EntityIdFactory.getByTypeAndId(entityType.name(), toUUID(entityId).toString()));
        }
        if (userId != null) {
            auditLog.setUserId(new UserId(toUUID(entityId)));
        }
        auditLog.setEntityName(this.entityName);
        auditLog.setUserName(this.userName);
        auditLog.setActionType(this.actionType);
        auditLog.setActionData(this.actionData);
        auditLog.setActionStatus(this.actionStatus);
        auditLog.setActionFailureDetails(this.actionFailureDetails);
        return auditLog;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public JsonNode getActionData() {
        return actionData;
    }

    public void setActionData(JsonNode actionData) {
        this.actionData = actionData;
    }

    public ActionStatus getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(ActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getActionFailureDetails() {
        return actionFailureDetails;
    }

    public void setActionFailureDetails(String actionFailureDetails) {
        this.actionFailureDetails = actionFailureDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AuditLogEntity that = (AuditLogEntity) o;
        return Objects.equals(tenantId, that.tenantId) &&
                Objects.equals(customerId, that.customerId) &&
                entityType == that.entityType &&
                Objects.equals(entityId, that.entityId) &&
                Objects.equals(entityName, that.entityName) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(userName, that.userName) &&
                actionType == that.actionType &&
                Objects.equals(actionData, that.actionData) &&
                actionStatus == that.actionStatus &&
                Objects.equals(actionFailureDetails, that.actionFailureDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tenantId, customerId, entityType, entityId, entityName, userId, userName, actionType, actionData, actionStatus, actionFailureDetails);
    }
}
