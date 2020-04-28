package open.iot.server.dao.sql.asset;


import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.EntitySubtype;
import open.iot.server.common.data.EntityType;
import open.iot.server.common.data.asset.Asset;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.dao.DaoUtil;
import open.iot.server.dao.asset.AssetDao;
import open.iot.server.dao.model.sql.AssetEntity;
import open.iot.server.dao.sql.JpaAbstractSearchTextDao;
import open.iot.server.dao.util.SqlDao;
import open.iot.server.dao.model.ModelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.*;

import static open.iot.server.common.data.UUIDConverter.fromTimeUUID;
import static open.iot.server.common.data.UUIDConverter.fromTimeUUIDs;

/**
 * @author james mu
 * @date 19-1-8 下午4:14
 */
@Component
@SqlDao
public class JpaAssetDao extends JpaAbstractSearchTextDao<AssetEntity, Asset> implements AssetDao {

    @Autowired
    private AssetRepository assetRepository;


    @Override
    public List<Asset> findAssetsByTenantId(UUID tenantId, TextPageLink pageLink) {
        return DaoUtil.convertDataList(assetRepository
                .findByTenantId(
                        fromTimeUUID(tenantId),
                        Objects.toString(pageLink.getTextSearch(), ""),
                        pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                    PageRequest.of(0, pageLink.getLimit())));
    }

    @Override
    public List<Asset> findAssetsByTenantIdAndType(UUID tenantId, String type, TextPageLink pageLink) {
        return DaoUtil.convertDataList(assetRepository
                .findByTenantIdAndType(
                        fromTimeUUID(tenantId),
                        type,
                        Objects.toString(pageLink.getTextSearch(), ""),
                        pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                    PageRequest.of(0, pageLink.getLimit())));
    }

    @Override
    public ListenableFuture<List<Asset>> findAssetsByTenantIdAndIdsAsync(UUID tenantId, List<UUID> assetIds) {
        return service.submit(() ->
                DaoUtil.convertDataList(assetRepository.findByTenantIdAndIdIn(fromTimeUUID(tenantId), fromTimeUUIDs(assetIds))));
    }

    @Override
    public List<Asset> findAssetsByTenantIdAndCustomerId(UUID tenantId, UUID customerId, TextPageLink pageLink) {
        return DaoUtil.convertDataList(assetRepository
                .findByTenantIdAndCustomerId(
                        fromTimeUUID(tenantId),
                        fromTimeUUID(customerId),
                        Objects.toString(pageLink.getTextSearch(), ""),
                        pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                    PageRequest.of(0, pageLink.getLimit())));
    }

    @Override
    public List<Asset> findAssetsByTenantIdAndCustomerIdAndType(UUID tenantId, UUID customerId, String type, TextPageLink pageLink) {
        return DaoUtil.convertDataList(assetRepository
                .findByTenantIdAndCustomerIdAndType(
                        fromTimeUUID(tenantId),
                        fromTimeUUID(customerId),
                        type,
                        Objects.toString(pageLink.getTextSearch(), ""),
                        pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : fromTimeUUID(pageLink.getIdOffset()),
                    PageRequest.of(0, pageLink.getLimit())));
    }

    @Override
    public ListenableFuture<List<Asset>> findAssetsByTenantIdAndCustomerIdAndIdsAsync(UUID tenantId, UUID customerId, List<UUID> assetIds) {
        return service.submit(() ->
                DaoUtil.convertDataList(assetRepository.findByTenantIdAndCustomerIdAndIdIn(fromTimeUUID(tenantId), fromTimeUUID(customerId), fromTimeUUIDs(assetIds))));
    }

    @Override
    public Optional<Asset> findAssetsByTenantIdAndName(UUID tenantId, String name) {
        Asset asset = DaoUtil.getData(assetRepository.findByTenantIdAndName(fromTimeUUID(tenantId), name));
        return Optional.ofNullable(asset);
    }

    @Override
    public ListenableFuture<List<EntitySubtype>> findTenantAssetTypesAsync(UUID tenantId) {
        return service.submit(() -> convertTenantAssetTypesToDto(tenantId, assetRepository.findTenantAssetTypes(fromTimeUUID(tenantId))));
    }

    private List<EntitySubtype> convertTenantAssetTypesToDto(UUID tenantId, List<String> types) {
        List<EntitySubtype> list = Collections.emptyList();
        if (types != null && !types.isEmpty()) {
            list = new ArrayList<>();
            for (String type : types) {
                list.add(new EntitySubtype(new TenantId(tenantId), EntityType.ASSET, type));
            }
        }
        return list;
    }

    @Override
    protected Class<AssetEntity> getEntityClass() {
        return AssetEntity.class;
    }

    @Override
    protected CrudRepository<AssetEntity, String> getCrudRepository() {
        return assetRepository;
    }
}
