package open.iot.server.dao.relation;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.relation.EntityRelation;
import open.iot.server.common.data.relation.EntityRelationInfo;
import open.iot.server.common.data.relation.EntityRelationsQuery;
import open.iot.server.common.data.relation.RelationTypeGroup;
import open.iot.server.dao.util.SqlDao;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@SqlDao
public class BaseRelationService implements RelationService {

    @Override
    public ListenableFuture<Boolean> checkRelation(EntityId from, EntityId to, String relationType, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public EntityRelation getRelation(EntityId from, EntityId to, String relationType, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public ListenableFuture<EntityRelation> getRelationAsync(EntityId from, EntityId to, String relationType, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public boolean saveRelation(EntityRelation relation) {
        return false;
    }

    @Override
    public ListenableFuture<Boolean> saveRelationAsync(EntityRelation relation) {
        return null;
    }

    @Override
    public boolean deleteRelation(EntityRelation relation) {
        return false;
    }

    @Override
    public ListenableFuture<Boolean> deleteRelationAsync(EntityRelation relation) {
        return null;
    }

    @Override
    public boolean deleteRelation(EntityId from, EntityId to, String relationType, RelationTypeGroup typeGroup) {
        return false;
    }

    @Override
    public ListenableFuture<Boolean> deleteRelationAsync(EntityId from, EntityId to, String relationType, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public void deleteEntityRelations(EntityId entity) {

    }

    @Override
    public ListenableFuture<Void> deleteEntityRelationsAsync(EntityId entity) {
        return null;
    }

    @Override
    public List<EntityRelation> findByFrom(EntityId from, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public ListenableFuture<List<EntityRelation>> findByFromAsync(EntityId from, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public ListenableFuture<List<EntityRelationInfo>> findInfoByFrom(EntityId from, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public List<EntityRelation> findByFromAndType(EntityId from, String relationType, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public ListenableFuture<List<EntityRelation>> findByFromAndTypeAsync(EntityId from, String relationType, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public List<EntityRelation> findByTo(EntityId to, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public ListenableFuture<List<EntityRelation>> findByToAsync(EntityId to, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public ListenableFuture<List<EntityRelationInfo>> findInfoByTo(EntityId to, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public List<EntityRelation> findByToAndType(EntityId to, String relationType, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public ListenableFuture<List<EntityRelation>> findByToAndTypeAsync(EntityId to, String relationType, RelationTypeGroup typeGroup) {
        return null;
    }

    @Override
    public ListenableFuture<List<EntityRelation>> findByQuery(EntityRelationsQuery query) {
        return null;
    }

    @Override
    public ListenableFuture<List<EntityRelationInfo>> findInfoByQuery(EntityRelationsQuery query) {
        return null;
    }
}
