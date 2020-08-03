package open.iot.server.dao.tenant;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.Tenant;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.page.TextPageData;
import open.iot.server.common.data.page.TextPageLink;


public interface TenantService {

    Tenant findTenantById(TenantId tenantId);

    ListenableFuture<Tenant> findTenantByIdAsync(TenantId customerId);

    Tenant saveTenant(Tenant tenant);

    void deleteTenant(TenantId tenantId);

    TextPageData<Tenant> findTenants(TextPageLink pageLink);

    void deleteTenants();
}
