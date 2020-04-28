package open.iot.server.dao.tenant;

import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.Tenant;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.page.TextPageData;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.dao.entity.AbstractEntityService;
import open.iot.server.dao.exception.DataValidationException;
import open.iot.server.dao.service.DataValidator;
import open.iot.server.dao.service.PaginatedRemover;
import open.iot.server.dao.service.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static open.iot.server.dao.service.Validator.validateId;

/**
 * @author james mu
 * @date 19-1-24 下午3:42
 */
@Service
@Slf4j
public class TenantServiceImpl extends AbstractEntityService implements TenantService{

    private static final String DEFAULT_TENANT_REGION = "Global";
    public static final String INCORRECT_TENANT_ID = "Incorrect tenantId ";

    @Autowired
    private TenantDao tenantDao;

    @Override
    public Tenant findTenantById(TenantId tenantId) {
        log.trace("Executing findTenantById [{}]",tenantId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        return tenantDao.findById(tenantId.getId());
    }

    @Override
    public ListenableFuture<Tenant> findTenantByIdAsync(TenantId tenantId) {
        log.trace("Executing TenantIdAsync [{}]", tenantId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        return tenantDao.findByIdAsync(tenantId.getId());
    }

    @Override
    public Tenant saveTenant(Tenant tenant) {
        log.trace("Executing saveTenant [{}]", tenant);
        tenant.setRegion(DEFAULT_TENANT_REGION);
        tenantValidator.validate(tenant);
        return tenantDao.save(tenant);
    }

    // TODO  tenant need something to do
    @Override
    public void deleteTenant(TenantId tenantId) {

    }

    @Override
    public TextPageData<Tenant> findTenants(TextPageLink pageLink) {
        log.trace("Executing findTenants pageLink [{}]", pageLink);
        Validator.validatePageLink(pageLink, "Incorrect page link " + pageLink);
        List<Tenant> tenants = tenantDao.findTenantsByRegion(DEFAULT_TENANT_REGION, pageLink);
        return new TextPageData<>(tenants, pageLink);
    }

    @Override
    public void deleteTenants() {
        log.trace("Executing deleteTenants");
        tenantsRemover.removeEntities(DEFAULT_TENANT_REGION);

    }

    private DataValidator<Tenant> tenantValidator =
            new DataValidator<Tenant>() {
                @Override
                protected void validateDataImpl(Tenant tenant) {
                    if (StringUtils.isEmpty(tenant.getTitle())) {
                        throw new DataValidationException("Tenant title should be specified!");
                    }
                    if (!StringUtils.isEmpty(tenant.getEmail())) {
                        validateEmail(tenant.getEmail());
                    }
                }
    };

    private PaginatedRemover<String, Tenant> tenantsRemover =
            new PaginatedRemover<String, Tenant>() {

                @Override
                protected List<Tenant> findEntities(String region, TextPageLink pageLink) {
                    return tenantDao.findTenantsByRegion(region, pageLink);
                }

                @Override
                protected void removeEntity(Tenant entity) {
                    deleteTenant(new TenantId(entity.getUuidId()));
                }
            };
}
