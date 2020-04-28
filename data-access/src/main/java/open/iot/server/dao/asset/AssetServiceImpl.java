package open.iot.server.dao.asset;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.common.data.Customer;
import open.iot.server.common.data.EntitySubtype;
import open.iot.server.common.data.EntityType;
import open.iot.server.common.data.Tenant;
import open.iot.server.common.data.asset.Asset;
import open.iot.server.common.data.asset.AssetSearchQuery;
import open.iot.server.common.data.id.AssetId;
import open.iot.server.common.data.id.CustomerId;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.page.TextPageData;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.common.data.relation.EntityRelation;
import open.iot.server.common.data.relation.EntitySearchDirection;
import open.iot.server.dao.customer.CustomerDao;
import open.iot.server.dao.entity.AbstractEntityService;
import open.iot.server.dao.exception.DataValidationException;
import open.iot.server.dao.service.DataValidator;
import open.iot.server.dao.service.PaginatedRemover;
import open.iot.server.dao.tenant.TenantDao;
import lombok.extern.slf4j.Slf4j;
import open.iot.server.dao.model.ModelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static open.iot.server.dao.DaoUtil.toUUIDs;
import static open.iot.server.dao.service.Validator.validateId;
import static open.iot.server.dao.service.Validator.validateIds;
import static open.iot.server.dao.service.Validator.validatePageLink;
import static open.iot.server.dao.service.Validator.validateString;

/**
 * @author james mu
 * @date 19-2-28 上午10:11
 * @description
 */
@Slf4j
@Service
public class AssetServiceImpl extends AbstractEntityService implements AssetService {

    public static final String INCORRECT_TENANT_ID = "Incorrect tenantId ";
    public static final String INCORRECT_PAGE_LINK = "Incorrect page link ";
    public static final String INCORRECT_CUSTOMER_ID = "Incorrect customerId ";
    public static final String INCORRECT_ASSET_ID = "Incorrect assetId ";

    @Autowired
    private AssetDao assetDao;

    @Autowired
    private TenantDao tenantDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    public Asset findAssetById(AssetId assetId) {
        log.trace("Executing findAssetById [{}]", assetId);
        validateId(assetId, INCORRECT_ASSET_ID + assetId);
        return assetDao.findById(assetId.getId());
    }

    @Override
    public ListenableFuture<Asset> findAssetByIdAsync(AssetId assetId) {
        log.trace("Executing findAssetById [{}]", assetId);
        validateId(assetId, INCORRECT_ASSET_ID + assetId);
        return assetDao.findByIdAsync(assetId.getId());
    }

    @Override
    public Optional<Asset> findAssetByTenantIdAndName(TenantId tenantId, String name) {
        log.trace("Executing findAssetByTenantIdAndName [{}][{}]", tenantId, name);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        return assetDao.findAssetsByTenantIdAndName(tenantId.getId(), name);
    }

    @Override
    public Asset saveAsset(Asset asset) {
        log.trace("Executing saveAsset [{}]", asset);
        assetValidator.validate(asset);
        return assetDao.save(asset);
    }

    @Override
    public Asset assignAssetToCustomer(AssetId assetId, CustomerId customerId) {
        Asset asset = findAssetById(assetId);
        asset.setCustomerId(customerId);
        return saveAsset(asset);
    }

    @Override
    public Asset unassignAssetFromCustomer(AssetId assetId) {
        Asset asset = findAssetById(assetId);
        asset.setCustomerId(null);
        return saveAsset(asset);
    }

    @Override
    public void deleteAsset(AssetId assetId) {
        log.trace("Executing deleteAsset [{}]", assetId);
        validateId(assetId, INCORRECT_ASSET_ID + assetId);
        deleteEntityRelations(assetId);
        assetDao.removeById(assetId.getId());
    }

    @Override
    public TextPageData<Asset> findAssetsByTenantId(TenantId tenantId, TextPageLink pageLink) {
        log.trace("Executing findAssetsByTenantId, tenantId [{}], pageLink [{}]", tenantId, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Asset> assets = assetDao.findAssetsByTenantId(tenantId.getId(), pageLink);
        return new TextPageData<>(assets, pageLink);
    }

    @Override
    public TextPageData<Asset> findAssetsByTenantIdAndType(TenantId tenantId, String type, TextPageLink pageLink) {
        log.trace("Executing findAssetsByTenantIdAndType, tenantId [{}], type [{}], pageLink [{}]", tenantId, type, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateString(type, "Incorrect type " + type);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Asset> assets = assetDao.findAssetsByTenantIdAndType(tenantId.getId(), type, pageLink);
        return new TextPageData<>(assets, pageLink);
    }

    @Override
    public ListenableFuture<List<Asset>> findAssetsByTenantIdAndIdsAsync(TenantId tenantId, List<AssetId> assetIds) {
        log.trace("Executing findAssetsByTenantIdAndIdsAsync, tenantId [{}], assetIds [{}]", tenantId, assetIds);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateIds(assetIds, "Incorrect assetIds " + assetIds);
        return assetDao.findAssetsByTenantIdAndIdsAsync(tenantId.getId(), toUUIDs(assetIds));
    }

    @Override
    public void deleteAssetsByTenantId(TenantId tenantId) {
        log.trace("Executing deleteAssetsByTenantId, tenantId [{}]", tenantId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        tenantAssetsRemover.removeEntities(tenantId);
    }

    @Override
    public TextPageData<Asset> findAssetsByTenantIdAndCustomerId(TenantId tenantId, CustomerId customerId, TextPageLink pageLink) {
        log.trace("Executing findAssetsByTenantIdAndCustomerId, tenantId [{}], customerId [{}], pageLink [{}]", tenantId, customerId, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Asset> assets = assetDao.findAssetsByTenantIdAndCustomerId(tenantId.getId(), customerId.getId(), pageLink);
        return new TextPageData<>(assets, pageLink);
    }

    @Override
    public TextPageData<Asset> findAssetsByTenantIdAndCustomerIdAndType(TenantId tenantId, CustomerId customerId, String type, TextPageLink pageLink) {
        log.trace("Executing findAssetsByTenantIdAndCustomerIdAndType, tenantId [{}], customerId [{}], type [{}], pageLink [{}]", tenantId, customerId, type, pageLink);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        validateString(type, "Incorrect type " + type);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Asset> assets = assetDao.findAssetsByTenantIdAndCustomerIdAndType(tenantId.getId(), customerId.getId(), type, pageLink);
        return new TextPageData<>(assets, pageLink);
    }

    @Override
    public ListenableFuture<List<Asset>> findAssetsByTenantIdCustomerIdAndIdsAsync(TenantId tenantId, CustomerId customerId, List<AssetId> assetIds) {
        log.trace("Executing findAssetsByTenantIdAndCustomerIdAndIdsAsync, tenantId [{}], customerId [{}], assetIds [{}]", tenantId, customerId, assetIds);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        validateIds(assetIds, "Incorrect assetIds " + assetIds);
        return assetDao.findAssetsByTenantIdAndCustomerIdAndIdsAsync(tenantId.getId(), customerId.getId(), toUUIDs(assetIds));
    }

    @Override
    public void unassignCustomerAssets(TenantId tenantId, CustomerId customerId) {
        log.trace("Executing unassignCustomerAssets, tenantId [{}], customerId [{}]", tenantId, customerId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        new CustomerAssetsUnassigner(tenantId).removeEntities(customerId);
    }

    @Override
    public ListenableFuture<List<EntitySubtype>> findAssetTypesByTenantId(TenantId tenantId) {
        log.trace("Executing findAssetTypesByTenantId, tenantId [{}]", tenantId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        ListenableFuture<List<EntitySubtype>> tenantAssetTypes = assetDao.findTenantAssetTypesAsync(tenantId.getId());
        return Futures.transform(tenantAssetTypes, assetTypes -> {
            assetTypes.sort(Comparator.comparing(EntitySubtype::getType));
            return assetTypes;
        }, Executors.newSingleThreadExecutor());
    }

    @Override
    public ListenableFuture<List<Asset>> findAssetsByQuery(AssetSearchQuery query) {
        ListenableFuture<List<EntityRelation>> relations = relationService.findByQuery(query.toEntitySearchQuery());
        ListenableFuture<List<Asset>> assets = Futures.transformAsync(relations, r -> {
            EntitySearchDirection direction = query.toEntitySearchQuery().getParameters().getDirection();
            List<ListenableFuture<Asset>> futures = new ArrayList<>();
            for (EntityRelation relation : r) {
                EntityId entityId = direction == EntitySearchDirection.FROM ? relation.getTo() : relation.getFrom();
                if (entityId.getEntityType() == EntityType.ASSET) {
                    futures.add(findAssetByIdAsync(new AssetId(entityId.getId())));
                }
            }
            return Futures.successfulAsList(futures);
        }, Executors.newSingleThreadExecutor());
        assets = Futures.transform(assets, assetList ->
                assetList == null ? Collections.emptyList() : assetList.stream().filter(asset -> query.getAssetTypes().contains(asset.getType())).collect(Collectors.toList())
            , Executors.newSingleThreadExecutor());
        return assets;
    }

    private DataValidator<Asset> assetValidator =
        new DataValidator<>() {
            @Override
            protected void validateCreate(Asset asset) {
                assetDao.findAssetsByTenantIdAndName(asset.getTenantId().getId(), asset.getName()).ifPresent(
                    d -> {
                        throw new DataValidationException("Asset with such name already exists!");
                    }
                );
            }

            @Override
            protected void validateUpdate(Asset asset) {
                assetDao.findAssetsByTenantIdAndName(asset.getTenantId().getId(), asset.getName()).ifPresent(
                    d -> {
                        if (!d.getId().equals(asset.getId())) {
                            throw new DataValidationException("Asset with such name already exists!");
                        }
                    }
                );
            }

            @Override
            protected void validateDataImpl(Asset asset) {
                if (StringUtils.isEmpty(asset.getType())) {
                    throw new DataValidationException("Asset type should be specified!");
                }
                if (StringUtils.isEmpty(asset.getName())) {
                    throw new DataValidationException("Asset name should be specified!");
                }
                if (asset.getTenantId() == null) {
                    throw new DataValidationException("Asset should be assigned to tenant!");
                } else {
                    Tenant tenant = tenantDao.findById(asset.getTenantId().getId());
                    if (tenant == null) {
                        throw new DataValidationException("Asset is referencing to non-existent tenant!");
                    }
                }
                if (asset.getCustomerId() == null) {
                    asset.setCustomerId(new CustomerId(ModelConstants.NULL_UUID));
                } else if (!asset.getCustomerId().getId().equals(ModelConstants.NULL_UUID)) {
                    Customer customer = customerDao.findById(asset.getCustomerId().getId());
                    if (customer == null) {
                        throw new DataValidationException("Can't assign asset to non-existent customer!");
                    }
                    if (!customer.getTenantId().equals(asset.getTenantId())) {
                        throw new DataValidationException("Can't assign asset to customer from different tenant!");
                    }
                }
            }
        };

    private PaginatedRemover<TenantId, Asset> tenantAssetsRemover =
        new PaginatedRemover<TenantId, Asset>() {

            @Override
            protected List<Asset> findEntities(TenantId id, TextPageLink pageLink) {
                return assetDao.findAssetsByTenantId(id.getId(), pageLink);
            }

            @Override
            protected void removeEntity(Asset entity) {
                deleteAsset(new AssetId(entity.getId().getId()));
            }
        };

    class CustomerAssetsUnassigner extends PaginatedRemover<CustomerId, Asset> {

        private TenantId tenantId;

        CustomerAssetsUnassigner(TenantId tenantId) {
            this.tenantId = tenantId;
        }

        @Override
        protected List<Asset> findEntities(CustomerId id, TextPageLink pageLink) {
            return assetDao.findAssetsByTenantIdAndCustomerId(tenantId.getId(), id.getId(), pageLink);
        }

        @Override
        protected void removeEntity(Asset entity) {
            unassignAssetFromCustomer(new AssetId(entity.getId().getId()));
        }
    }
}
