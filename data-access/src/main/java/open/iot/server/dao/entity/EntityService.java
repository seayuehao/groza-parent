package open.iot.server.dao.entity;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.id.EntityId;

/**
 * @author james mu
 * @date 19-1-22 上午10:59
 */
public interface EntityService {

    ListenableFuture<String> fetchEntityNameAsync(EntityId entityId);

    void deleteEntityRelations(EntityId entityId);
}
