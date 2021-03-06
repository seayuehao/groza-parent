package open.iot.server.dao.event;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.Event;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.page.TimePageData;
import open.iot.server.common.data.page.TimePageLink;

import java.util.List;
import java.util.Optional;

/**
 * @author james mu
 * @date 19-1-8 上午11:19
 */
public interface EventService {

    Event save(Event event);

    ListenableFuture<Event> saveAsync(Event event);

    Optional<Event> saveIfNotExists(Event event);

    Optional<Event> findEvent(TenantId tenantId, EntityId entityId, String eventType, String eventUid);

    TimePageData<Event> findEvents(TenantId tenantId, EntityId entityId, TimePageLink pageLink);

    TimePageData<Event> findEvents(TenantId tenantId, EntityId entityId, String eventType, TimePageLink pageLink);

    List<Event> findLatestEvents(TenantId tenantId, EntityId entityId, String eventType, int limit);

}
