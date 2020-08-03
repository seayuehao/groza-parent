package open.iot.server.controller;

import open.iot.server.common.data.Tenant;
import open.iot.server.common.data.exception.GrozaException;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.page.TextPageData;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.dao.tenant.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TenantController extends BaseController {

    @Autowired
    private TenantService tenantService;

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "/tenant/{tenantId}", method = RequestMethod.GET)
    @ResponseBody
    public Tenant getTenantById(@PathVariable("tenantId") String strTenantId) throws GrozaException {
        checkParameter("tenantId", strTenantId);
        try {
            TenantId tenantId = new TenantId(toUUID(strTenantId));
            return checkNotNull(tenantService.findTenantById(tenantId));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/tenant", method = RequestMethod.POST)
    public Tenant saveTenant(@RequestBody Tenant tenant) throws GrozaException {
        try {
            boolean newTenant = tenant.getId() == null;
            tenant = checkNotNull(tenantService.saveTenant(tenant));
            if (newTenant) {
            }
            return tenant;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @RequestMapping(value = "/tenants", params = {"limit"}, method = RequestMethod.GET)
    @ResponseBody
    public TextPageData<Tenant> getTenants(@RequestParam int limit,
                                           @RequestParam(required = false) String textSearch,
                                           @RequestParam(required = false) String idOffset,
                                           @RequestParam(required = false) String textOffset) throws GrozaException {
        try {
            TextPageLink pageLink = createPageLink(limit, textSearch, idOffset, textOffset);
            return checkNotNull(tenantService.findTenants(pageLink));
        } catch (Exception e) {
            throw handleException(e);
        }
    }
}
