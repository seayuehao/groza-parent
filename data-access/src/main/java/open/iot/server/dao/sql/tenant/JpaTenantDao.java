package open.iot.server.dao.sql.tenant;

import open.iot.server.common.data.Tenant;
import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.dao.DaoUtil;
import open.iot.server.dao.model.sql.TenantEntity;
import open.iot.server.dao.sql.JpaAbstractSearchTextDao;
import open.iot.server.dao.tenant.TenantDao;
import open.iot.server.dao.util.SqlDao;
import open.iot.server.dao.model.ModelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;


@Component
@SqlDao
public class JpaTenantDao extends JpaAbstractSearchTextDao<TenantEntity, Tenant> implements TenantDao {

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    protected Class<TenantEntity> getEntityClass() {
        return TenantEntity.class;
    }

    @Override
    protected CrudRepository<TenantEntity, String> getCrudRepository() {
        return tenantRepository;
    }

    @Override
    public List<Tenant> findTenantsByRegion(String region, TextPageLink pageLink) {
        return DaoUtil.convertDataList(tenantRepository
                .findByRegionNextPage(
                        region,
                        Objects.toString(pageLink.getTextSearch(), ""),
                        pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : UUIDConverter.fromTimeUUID(pageLink.getIdOffset()),
                    PageRequest.of(0, pageLink.getLimit())));
    }
}
