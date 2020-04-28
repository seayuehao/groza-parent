package open.iot.server.common.data;

import com.fasterxml.jackson.databind.JsonNode;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.EventId;
import open.iot.server.common.data.id.TenantId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author james mu
 * @date 19-1-8 上午11:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Event extends BaseData<EventId> {

    private TenantId tenantId;
    private String type;
    private String uid;
    private EntityId entityId;
    private transient JsonNode body;

    public Event(){
        super();
    }

    public Event(EventId id) {
        super(id);
    }

    public Event(Event event) {
        super(event);
    }
}
