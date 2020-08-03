package open.iot.server.dao.sql.event;

import com.datastax.driver.core.utils.UUIDs;
import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.Event;
import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.EventId;
import open.iot.server.common.data.page.TimePageLink;
import open.iot.server.dao.DaoUtil;
import open.iot.server.dao.event.EventDao;
import open.iot.server.dao.model.ModelConstants;
import open.iot.server.dao.model.sql.EventEntity;
import open.iot.server.dao.sql.JpaAbstractSearchTimeDao;
import open.iot.server.dao.util.SqlDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.jpa.domain.Specification.where;


@SqlDao
@Component
public class JpaBaseEventDao extends JpaAbstractSearchTimeDao<EventEntity, Event> implements EventDao {

    private static final Logger log = LoggerFactory.getLogger("JpaBaseEventDao");

    private final UUID systemTenantId = ModelConstants.NULL_UUID;

    @Autowired
    private EventRepository eventRepository;

    @Override
    protected Class<EventEntity> getEntityClass() {
        return EventEntity.class;
    }

    @Override
    protected CrudRepository<EventEntity, String> getCrudRepository() {
        return eventRepository;
    }

    @Override
    public Event save(Event event) {
        log.debug("Save event [{}] ", event);
        if (event.getId() == null) {
            event.setId(new EventId(UUIDs.timeBased()));
        }
        if (StringUtils.isEmpty(event.getUid())) {
            event.setUid(event.getId().toString());
        }
        return save(new EventEntity(event), false).orElse(null);
    }

    @Override
    public ListenableFuture<Event> saveAsync(Event event) {
        log.debug("Save event [{}] ", event);
        if (event.getId() == null) {
            event.setId(new EventId(UUIDs.timeBased()));
        }
        if (StringUtils.isEmpty(event.getUid())) {
            event.setUid(event.getId().toString());
        }
        return service.submit(() -> save(new EventEntity(event), false).orElse(null));
    }

    @Override
    public Optional<Event> saveIfNotExists(Event event) {
        return save(new EventEntity(event), true);
    }

    @Override
    public Event findEvent(UUID tenantId, EntityId entityId, String eventType, String eventUid) {
        return DaoUtil.getData(eventRepository.findByTenantIdAndEntityTypeAndEntityIdAndEventTypeAndEventUid(
                UUIDConverter.fromTimeUUID(tenantId), entityId.getEntityType(), UUIDConverter.fromTimeUUID(entityId.getId()), eventType, eventUid));
    }

    @Override
    public List<Event> findEvents(UUID tenantId, EntityId entityId, TimePageLink pageLink) {
        return findEvents(tenantId, entityId, null, pageLink);
    }

    @Override
    public List<Event> findEvents(UUID tenantId, EntityId entityId, String eventType, TimePageLink pageLink) {
        Specification<EventEntity> timeSearchSpec = JpaAbstractSearchTimeDao.getTimeSearchPageSpec(pageLink, "id");
        Specification<EventEntity> fieldsSpec = getEntityFieldsSpec(tenantId, entityId, eventType);
        Sort.Direction sortDirection = pageLink.isAscOrder() ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(0, pageLink.getLimit(), sortDirection, ModelConstants.ID_PROPERTY);
        return DaoUtil.convertDataList(eventRepository.findAll(where(timeSearchSpec).and(fieldsSpec), pageable).getContent());
    }

    @Override
    public List<Event> findLatestEvents(UUID tenantId, EntityId entityId, String eventType, int limit) {
        List<EventEntity> latest = eventRepository.findLatestByTenantIdAndEntityTypeAndEntityIdAndEventType(
                UUIDConverter.fromTimeUUID(tenantId),
                entityId.getEntityType(),
                UUIDConverter.fromTimeUUID(entityId.getId()),
                eventType, PageRequest.of(0, limit));
        return DaoUtil.convertDataList(latest);
    }

    public Optional<Event> save(EventEntity entity, boolean ifNotExists) {
        log.debug("Save event [{}] ", entity);
        if (entity.getTenantId() == null) {
            log.trace("Save system event with predefined id {}", systemTenantId);
            entity.setTenantId(UUIDConverter.fromTimeUUID(systemTenantId));
        }
        if (entity.getId() == null) {
            entity.setId(UUIDs.timeBased());
        }
        if (StringUtils.isEmpty(entity.getEventUid())) {
            entity.setEventUid(entity.getId().toString());
        }
        if (ifNotExists &&
                eventRepository.findByTenantIdAndEntityTypeAndEntityId(entity.getTenantId(), entity.getEntityType(), entity.getEntityId()) != null) {
            return Optional.empty();
        }
        return Optional.of(DaoUtil.getData(eventRepository.save(entity)));
    }

    private Specification<EventEntity> getEntityFieldsSpec(UUID tenantId, EntityId entityId, String eventType) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (tenantId != null) {
                Predicate tenantIdPredicate = criteriaBuilder.equal(root.get("tenantId"), UUIDConverter.fromTimeUUID(tenantId));
                predicates.add(tenantIdPredicate);
            }
            if (entityId != null) {
                Predicate entityTypePredicate = criteriaBuilder.equal(root.get("entityType"), entityId.getEntityType());
                predicates.add(entityTypePredicate);
                Predicate entityIdPredicate = criteriaBuilder.equal(root.get("entityId"), UUIDConverter.fromTimeUUID(entityId.getId()));
                predicates.add(entityIdPredicate);
            }
            if (eventType != null) {
                Predicate eventTypePredicate = criteriaBuilder.equal(root.get("eventType"), eventType);
                predicates.add(eventTypePredicate);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
