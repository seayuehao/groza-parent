package open.iot.server.dao.sql.device;

import open.iot.server.common.data.UUIDConverter;
import open.iot.server.common.data.security.DeviceCredentials;
import open.iot.server.dao.DaoUtil;
import open.iot.server.dao.device.DeviceCredentialsDao;
import open.iot.server.dao.model.sql.DeviceCredentialsEntity;
import open.iot.server.dao.sql.JpaAbstractDao;
import open.iot.server.dao.util.SqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author james mu
 * @date 18-12-24 下午4:50
 */
@SqlDao
@Component
public class JpaDeviceCredentialsDao extends JpaAbstractDao<DeviceCredentialsEntity, DeviceCredentials> implements DeviceCredentialsDao {

    @Autowired
    private DeviceCredentialsRepository deviceCredentialsRepository;

    @Override
    public DeviceCredentials findByDeviceId(UUID deviceId) {
        return DaoUtil.getData(deviceCredentialsRepository.findByDeviceId(UUIDConverter.fromTimeUUID(deviceId)));
    }

    @Override
    public DeviceCredentials findByCredentialsId(String credentialsId) {
        return DaoUtil.getData(deviceCredentialsRepository.findByCredentialsId(credentialsId));
    }

    @Override
    protected Class<DeviceCredentialsEntity> getEntityClass() {
        return DeviceCredentialsEntity.class;
    }

    @Override
    protected CrudRepository<DeviceCredentialsEntity, String> getCrudRepository() {
        return deviceCredentialsRepository;
    }
}
