package open.iot.server.dao.sql.customer;

import open.iot.server.common.data.Customer;
import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.page.TextPageLink;
import open.iot.server.dao.DaoUtil;
import open.iot.server.dao.customer.CustomerDao;
import open.iot.server.dao.model.sql.CustomerEntity;
import open.iot.server.dao.sql.JpaAbstractSearchTextDao;
import open.iot.server.dao.util.SqlDao;
import open.iot.server.dao.model.ModelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Component
@SqlDao
public class JpaCustomerDao extends JpaAbstractSearchTextDao<CustomerEntity, Customer> implements CustomerDao {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findCustomersByTenantId(UUID tenantId, TextPageLink pageLink) {
        return DaoUtil.convertDataList(customerRepository.findByTenantId(
                UUIDConverter.fromTimeUUID(tenantId),
                Objects.toString(pageLink.getTextSearch(), ""),
                pageLink.getIdOffset() == null ? ModelConstants.NULL_UUID_STR : UUIDConverter.fromTimeUUID(pageLink.getIdOffset()),
            PageRequest.of(0, pageLink.getLimit())));
    }

    @Override
    public Optional<Customer> findCustomersByTenantIdAndTitle(UUID tenantId, String title) {
        Customer customer = DaoUtil.getData(customerRepository.findByTenantIdAndTitle(UUIDConverter.fromTimeUUID(tenantId), title));
        return Optional.ofNullable(customer);
    }

    @Override
    protected Class<CustomerEntity> getEntityClass() {
        return CustomerEntity.class;
    }

    @Override
    protected CrudRepository<CustomerEntity, String> getCrudRepository() {
        return customerRepository;
    }
}
