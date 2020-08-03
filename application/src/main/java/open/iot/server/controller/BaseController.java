package open.iot.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import open.iot.server.common.data.BaseData;
import open.iot.server.common.data.Customer;
import open.iot.server.common.data.Device;
import open.iot.server.common.data.HasName;
import open.iot.server.common.data.User;
import open.iot.server.common.data.asset.Asset;
import open.iot.server.common.data.audit.ActionType;
import open.iot.server.common.data.exception.GrozaErrorCode;
import open.iot.server.common.data.exception.GrozaException;
import open.iot.server.common.data.id.AssetId;
import open.iot.server.common.data.id.CustomerId;
import open.iot.server.common.data.id.DeviceId;
import open.iot.server.common.data.id.EntityId;
import open.iot.server.common.data.id.TenantId;
import open.iot.server.common.data.id.UUIDBased;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.common.data.page.TimePageLink;
import open.iot.server.common.data.security.Authority;
import open.iot.server.dao.asset.AssetService;
import open.iot.server.dao.customer.CustomerService;
import open.iot.server.dao.device.DeviceService;
import open.iot.server.dao.exception.DataValidationException;
import open.iot.server.dao.exception.IncorrectParameterException;
import open.iot.server.dao.model.ModelConstants;
import open.iot.server.dao.tenant.TenantService;
import open.iot.server.service.security.model.SecurityUser;
import open.iot.server.service.state.DeviceStateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.mail.MessagingException;
import java.util.UUID;

import static open.iot.server.dao.service.Validator.validateId;


public abstract class BaseController {

    private static final Logger log = LoggerFactory.getLogger("BaseController");

    public static final String INCORRECT_TENANT_ID = "Incorrect tenantId ";
    public static final String YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION = "You don't have permission to perform this operation!";

    private static final ObjectMapper json = new ObjectMapper();

    @Autowired
    protected TenantService tenantService;

    @Autowired
    protected AssetService assetService;

    @Autowired
    protected CustomerService customerService;

    @Autowired
    protected DeviceService deviceService;

    @Autowired
    protected DeviceStateService deviceStateService;


    <T> T checkNotNull(T reference) throws GrozaException {
        if (reference == null) {
            throw new GrozaException("Requested item wasn't found!", GrozaErrorCode.ITEM_NOT_FOUND);
        }
        return reference;
    }

    void checkParameter(String name, String param) throws GrozaException {
        if (StringUtils.isEmpty(param)) {
            throw new GrozaException("Parameter '" + name + "' can't be empty!", GrozaErrorCode.BAD_REQUEST_PARAMS);
        }
    }

    Asset checkAssetId(AssetId assetId) throws GrozaException {
        try {
            validateId(assetId, "Incorrect assetId " + assetId);
            Asset asset = assetService.findAssetById(assetId);
            checkAsset(asset);
            return asset;
        } catch (Exception e) {
            throw handleException(e, false);
        }
    }

    protected void checkAsset(Asset asset) throws GrozaException {
        checkNotNull(asset);
        checkTenantId(asset.getTenantId());
        if (asset.getCustomerId() != null && !asset.getCustomerId().getId().equals(ModelConstants.NULL_UUID)) {
            checkCustomerId(asset.getCustomerId());
        }
    }

    Device checkDeviceId(DeviceId deviceId) throws GrozaException {
        try {
            validateId(deviceId, "Incorrect deviceId " + deviceId);
            Device device = deviceService.findDeviceById(deviceId);
            checkDevice(device);
            return device;
        } catch (Exception e) {
            throw handleException(e, false);
        }

    }

    protected void checkDevice(Device device) throws GrozaException {
        checkNotNull(device);
        checkTenantId(device.getTenantId());
        if (device.getCustomerId() != null && !device.getCustomerId().getId().equals(ModelConstants.NULL_UUID)) {
            checkCustomerId(device.getCustomerId());
        }
    }

    Customer checkCustomerId(CustomerId customerId) throws GrozaException {
        try {
            validateId(customerId, "Incorrect customerId " + customerId);
            SecurityUser authUser = getCurrentUser();
            if (authUser.getAuthority() == Authority.SYS_ADMIN ||
                    (authUser.getAuthority() != Authority.TENANT_ADMIN &&
                            (authUser.getCustomerId() == null || !authUser.getCustomerId().equals(customerId)))) {
                throw new GrozaException(YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION,
                        GrozaErrorCode.PERMISSION_DENIED);
            }
            Customer customer = customerService.findCustomerById(customerId);
            checkCustomer(customer);
            return customer;
        } catch (Exception e) {
            throw handleException(e, false);
        }
    }

    private void checkCustomer(Customer customer) throws GrozaException {
        checkNotNull(customer);
        checkTenantId(customer.getTenantId());
    }

    void checkTenantId(TenantId tenantId) throws GrozaException {
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        SecurityUser authUser = getCurrentUser();
        if (authUser.getAuthority() != Authority.SYS_ADMIN &&
                (authUser.getTenantId() == null || !authUser.getTenantId().equals(tenantId))) {
            throw new GrozaException(YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION,
                    GrozaErrorCode.PERMISSION_DENIED);
        }
    }

    protected SecurityUser getCurrentUser() throws GrozaException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            return (SecurityUser) authentication.getPrincipal();
        } else {
            throw new GrozaException("You aren't authorized to perform this operation!", GrozaErrorCode.AUTHENTICATION);
        }
    }

    TimePageLink createPageLink(int limit, Long startTime, Long endTime, boolean ascOrder, String idOffset) {
        UUID idOffsetUuid = null;
        if (StringUtils.isNotEmpty(idOffset)) {
            idOffsetUuid = toUUID(idOffset);
        }
        return new TimePageLink(limit, startTime, endTime, ascOrder, idOffsetUuid);
    }

    TextPageLink createPageLink(int limit, String textSearch, String idOffset, String textOffset) {
        UUID idOffsetUuid = null;
        if (StringUtils.isNotEmpty(idOffset)) {
            idOffsetUuid = toUUID(idOffset);
        }
        return new TextPageLink(limit, textSearch, idOffsetUuid, textOffset);
    }


    UUID toUUID(String id) {
        return UUID.fromString(id);
    }

    GrozaException handleException(Exception exception) {
        return handleException(exception, true);
    }

    private GrozaException handleException(Exception exception, boolean logException) {
        if (logException) {
            log.error("Error [{}]", exception.getMessage(), exception);
        }

        String cause = "";
        if (exception.getCause() != null) {
            cause = exception.getCause().getClass().getCanonicalName();
        }

        if (exception instanceof GrozaException) {
            return (GrozaException) exception;
        } else if (exception instanceof IllegalArgumentException || exception instanceof IncorrectParameterException
                || exception instanceof DataValidationException || cause.contains("IncorrectParameterException")) {
            return new GrozaException(exception.getMessage(), GrozaErrorCode.BAD_REQUEST_PARAMS);
        } else if (exception instanceof MessagingException) {
            return new GrozaException("Unable to send mail: " + exception.getMessage(), GrozaErrorCode.GENERAL);
        } else {
            return new GrozaException(exception.getMessage(), GrozaErrorCode.GENERAL);
        }
    }

    protected <E extends BaseData<I> & HasName,
            I extends UUIDBased & EntityId> void logEntityAction(I entityId, E entity, CustomerId customerId,
                                                                 ActionType actionType, Exception e, Object... additionalInfo) throws GrozaException {
        logEntityAction(getCurrentUser(), entityId, entity, customerId, actionType, e, additionalInfo);
    }

    protected <E extends BaseData<I> & HasName,
            I extends UUIDBased & EntityId> void logEntityAction(User user, I entityId, E entity, CustomerId customerId,
                                                                 ActionType actionType, Exception e, Object... additionalInfo) throws GrozaException {
        if (customerId == null || customerId.isNullUid()) {
            customerId = user.getCustomerId();
        }
    }

}
