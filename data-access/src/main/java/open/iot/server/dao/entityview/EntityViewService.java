package open.iot.server.dao.entityview;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.EntitySubtype;
import open.iot.server.common.data.EntityView;
import open.iot.server.common.data.entityview.EntityViewSearchQuery;
import open.iot.server.common.data.id.CustomerId;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.EntityViewId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.page.TextPageData;
import open.iot.server.common.data.page.TextPageLink;

import java.util.List;

/**
 * @author james mu
 * @date 19-1-24 下午2:11
 */
public interface EntityViewService {

    EntityView saveEntityView(EntityView entityView);

    EntityView assignEntityViewToCustomer(EntityViewId entityViewId, CustomerId customerId);

    EntityView unassignEntityViewFromCustomer(EntityViewId entityViewId);

    void unassignCustomerEntityViews(TenantId tenantId, CustomerId customerId);

    EntityView findEntityViewById(EntityViewId entityViewId);

    TextPageData<EntityView> findEntityViewByTenantId(TenantId tenantId, TextPageLink pageLink);

    TextPageData<EntityView> findEntityViewByTenantIdAndType(TenantId tenantId, TextPageLink pageLink, String type);

    TextPageData<EntityView> findEntityViewsByTenantIdAndCustomerId(TenantId tenantId, CustomerId customerId, TextPageLink pageLink);

    TextPageData<EntityView> findEntityViewsByTenantIdAndCustomerIdAndType(TenantId tenantId, CustomerId customerId, TextPageLink pageLink, String type);

    ListenableFuture<List<EntityView>> findEntityViewsByQuery(EntityViewSearchQuery query);

    ListenableFuture<EntityView> findEntityViewByIdAsync(EntityViewId entityViewId);

    ListenableFuture<List<EntityView>> findEntityViewsByTenantIdAndEntityIdAsync(TenantId tenantId, EntityId entityId);

    void deleteEntityView(EntityViewId entityViewId);

    void deleteEntityViewsByTenantId(TenantId tenantId);

    ListenableFuture<List<EntitySubtype>> findEntityViewTypesByTenantId(TenantId tenantId);
}
