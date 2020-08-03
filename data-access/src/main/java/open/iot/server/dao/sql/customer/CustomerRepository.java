package open.iot.server.dao.sql.customer;

import open.iot.server.dao.model.sql.CustomerEntity;
import open.iot.server.dao.util.SqlDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


@SqlDao
public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {

    @Query("SELECT c FROM CustomerEntity c WHERE c.tenantId = :tenantId " +
            "AND LOWER(c.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND c.id > :idOffset ORDER BY c.id")
    List<CustomerEntity> findByTenantId(@Param("tenantId") String tenantId,
                                        @Param("textSearch") String textSearch,
                                        @Param("idOffset") String idOffset,
                                        Pageable pageable);

    CustomerEntity findByTenantIdAndTitle(String tenantId, String title);
}
