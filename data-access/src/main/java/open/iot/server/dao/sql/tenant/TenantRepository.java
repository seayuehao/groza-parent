package open.iot.server.dao.sql.tenant;

import open.iot.server.dao.model.sql.TenantEntity;
import open.iot.server.dao.util.SqlDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


@SqlDao
public interface TenantRepository extends CrudRepository<TenantEntity, String> {

    @Query("SELECT t FROM TenantEntity t WHERE t.region = :region " +
            "AND LOWER(t.searchText) LIKE LOWER(CONCAT(:textSearch, '%')) " +
            "AND t.id > :idOffset ORDER BY t.id")
    List<TenantEntity> findByRegionNextPage(@Param("region") String region,
                                            @Param("textSearch") String textSearch,
                                            @Param("idOffset") String idOffset,
                                            Pageable pageable);
}
