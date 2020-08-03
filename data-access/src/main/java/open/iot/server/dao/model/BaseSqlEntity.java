package open.iot.server.dao.model;

import open.iot.server.common.data.UUIDConverter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;


@MappedSuperclass
public abstract class BaseSqlEntity<D> implements BaseEntity<D> {

    @Id
    @Column(name = ModelConstants.ID_PROPERTY)
    protected String id;

    @Override
    public UUID getId() {
        if (id == null) {
            return null;
        }
        return UUIDConverter.fromString(id);
    }

    @Override
    public void setId(UUID id) {
        this.id = UUIDConverter.fromTimeUUID(id);
    }


    protected UUID toUUID(String src) {
        return UUIDConverter.fromString(src);
    }

    protected String toString(UUID timeUUID) {
        return UUIDConverter.fromTimeUUID(timeUUID);
    }

}
