package open.iot.server.dao.relation;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.EntityType;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.page.TimePageLink;
import open.iot.server.common.data.relation.EntityRelation;
import open.iot.server.common.data.relation.RelationTypeGroup;

import java.util.List;


public interface RelationDao {

    ListenableFuture<List<EntityRelation>> findAllByFrom(EntityId from, RelationTypeGroup typeGroup);

    ListenableFuture<List<EntityRelation>> findAllByFromAndType(EntityId from, String relationType, RelationTypeGroup typeGroup);

    ListenableFuture<List<EntityRelation>> findAllByTo(EntityId to, RelationTypeGroup typeGroup);

    ListenableFuture<List<EntityRelation>> findAllByToAndType(EntityId to, String relationType, RelationTypeGroup typeGroup);

    ListenableFuture<Boolean> checkRelation(EntityId from, EntityId to, String relationType, RelationTypeGroup typeGroup);

    ListenableFuture<EntityRelation> getRelation(EntityId from, EntityId to, String relationType, RelationTypeGroup typeGroup);

    boolean saveRelation(EntityRelation relation);

    ListenableFuture<Boolean> saveRelationAsync(EntityRelation relation);

    boolean deleteOutboundRelations(EntityId entity);

    ListenableFuture<Boolean> deleteOutboundRelationsAsync(EntityId entity);

    ListenableFuture<List<EntityRelation>> findRelations(EntityId from, String relationType, RelationTypeGroup typeGroup, EntityType toType, TimePageLink pageLink);
}
