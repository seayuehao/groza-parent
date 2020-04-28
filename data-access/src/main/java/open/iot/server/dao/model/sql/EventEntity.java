package open.iot.server.dao.model.sql;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import open.iot.server.common.data.EntityType;
import open.iot.server.common.data.Event;
import open.iot.server.common.data.id.EntityIdFactory;
import open.iot.server.common.data.id.EventId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.dao.model.BaseEntity;
import open.iot.server.dao.model.BaseSqlEntity;
import open.iot.server.dao.util.mapping.JsonStringType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

import static open.iot.server.dao.model.ModelConstants.*;

/**
 * @author james mu
 * @date 19-1-8 下午3:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = EVENT_COLUMN_FAMILY_NAME)
@NoArgsConstructor
public class EventEntity extends BaseSqlEntity<Event> implements BaseEntity<Event> {

    @Column(name = EVENT_TENANT_ID_PROPERTY)
    private String tenantId;

    @Enumerated(EnumType.STRING)
    @Column(name = EVENT_ENTITY_TYPE_PROPERTY)
    private EntityType entityType;

    @Column(name = EVENT_ENTITY_ID_PROPERTY)
    private String entityId;

    @Column(name = EVENT_TYPE_PROPERTY)
    private String eventType;

    @Column(name = EVENT_UID_PROPERTY)
    private String eventUid;

    @Type(type = "json")
    @Column(name = EVENT_BODY_PROPERTY)
    private JsonNode body;

    public EventEntity(Event event) {
        if (event.getId() != null) {
            this.setId(event.getId().getId());
        }
        if (event.getTenantId() != null) {
            this.tenantId = toString(event.getTenantId().getId());
        }
        if (event.getEntityId() != null) {
            this.entityType = event.getEntityId().getEntityType();
            this.entityId = toString(event.getEntityId().getId());
        }
        this.eventType = event.getType();
        this.eventUid = event.getUid();
        this.body = event.getBody();
    }


    @Override
    public Event toData() {
        Event event = new Event(new EventId(getId()));
        event.setCreatedTime(UUIDs.unixTimestamp(getId()));
        event.setTenantId(new TenantId(toUUID(tenantId)));
        event.setEntityId(EntityIdFactory.getByTypeAndUuid(entityType, toUUID(entityId)));
        event.setBody(body);
        event.setType(eventType);
        event.setUid(eventUid);
        return event;
    }
}
