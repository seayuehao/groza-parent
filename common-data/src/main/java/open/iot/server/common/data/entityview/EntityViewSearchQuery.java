package open.iot.server.common.data.entityview;

import open.iot.server.common.data.EntityType;
import open.iot.server.common.data.relation.EntityRelation;
import open.iot.server.common.data.relation.EntityRelationsQuery;
import open.iot.server.common.data.relation.EntityTypeFilter;
import open.iot.server.common.data.relation.RelationsSearchParameters;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author james mu
 * @date 19-1-24 下午2:14
 */
@Data
public class EntityViewSearchQuery {

    private RelationsSearchParameters parameters;
    private String relationType;
    private List<String> entityViewTypes;

    public EntityRelationsQuery toEntitySearchQuery(){
        EntityRelationsQuery query = new EntityRelationsQuery();
        query.setParameters(parameters);
        query.setFilters(
                Collections.singletonList(new EntityTypeFilter(relationType == null ? EntityRelation.CONTAINS_TYPE : relationType,
                        Collections.singletonList(EntityType.ENTITY_VIEW))));
        return query;
    }
}
