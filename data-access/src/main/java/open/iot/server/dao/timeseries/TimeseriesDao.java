package open.iot.server.dao.timeseries;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.kv.TsKvEntry;
import open.iot.server.common.data.kv.TsKvQuery;

import java.util.List;

/**
 * @author james mu
 * @date 19-1-30 下午5:08
 * @description
 */
public interface TimeseriesDao {

    ListenableFuture<List<TsKvEntry>> findAllAsync(EntityId entityId, List<TsKvQuery> queries);

    ListenableFuture<TsKvEntry> findLatest(EntityId entityId, String key);

    ListenableFuture<List<TsKvEntry>> findAllLatest(EntityId entityId);

    ListenableFuture<Void> save(EntityId entityId, TsKvEntry tsKvEntry, long ttl);

    ListenableFuture<Void> savePartition(EntityId entityId, long tsKvEntryTs, String key, long ttl);

    ListenableFuture<Void> saveLatest(EntityId entityId, TsKvEntry tsKvEntry);
}
