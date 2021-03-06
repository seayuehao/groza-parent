package open.iot.server.dao.sql;

import com.datastax.driver.core.utils.UUIDs;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import open.iot.server.dao.Dao;
import open.iot.server.dao.DaoUtil;
import open.iot.server.dao.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static open.iot.server.common.data.UUIDConverter.fromTimeUUID;


public abstract class JpaAbstractDao<E extends BaseEntity<D>, D> extends JpaAbstractDaoListeningExecutorService implements Dao<D> {

    private static final Logger log = LoggerFactory.getLogger("JpaAbstractDao");

    protected abstract Class<E> getEntityClass();

    protected abstract CrudRepository<E, String> getCrudRepository();

    protected void setSearchText(E entity) {
    }

    @Override
    public List<D> find() {
        List<E> entities = Lists.newArrayList(getCrudRepository().findAll());
        return DaoUtil.convertDataList(entities);
    }

    @Override
    public D findById(UUID key) {
        log.debug("Get entity by key {}", key);
        E entity = getCrudRepository().findById(fromTimeUUID(key)).get();
        return DaoUtil.getData(entity);
    }

    @Override
    public ListenableFuture<D> findByIdAsync(UUID id) {
        log.debug("Get entity by key async {}", id);
        return service.submit(() -> DaoUtil.getData(getCrudRepository().findById(fromTimeUUID(id)).get()));
    }

    @Override
    @Transactional
    public D save(D domain) {
        E entity;
        try {
            entity = getEntityClass().getConstructor(domain.getClass()).newInstance(domain);
        } catch (Exception e) {
            log.error("Can't create entity for domain object {}", domain, e);
            throw new IllegalArgumentException("Can't create entity for domain object {" + domain + "}", e);
        }
        setSearchText(entity);
        log.debug("Saving entity {}", entity);
        if (entity.getId() == null) {
            entity.setId(UUIDs.timeBased());
        }
        entity = getCrudRepository().save(entity);
        return DaoUtil.getData(entity);
    }

    @Override
    @Transactional
    public boolean removeById(UUID id) {
        String key = fromTimeUUID(id);
        getCrudRepository().deleteById(key);
        log.debug("Remove request: {}", key);
        return getCrudRepository().findById(key).isPresent();
    }
}
