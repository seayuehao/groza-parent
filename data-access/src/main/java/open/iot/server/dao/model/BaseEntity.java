package open.iot.server.dao.model;

import java.util.UUID;


public interface BaseEntity<D> extends ToData<D> {

    UUID getId();

    void setId(UUID id);
}
