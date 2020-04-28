package open.iot.server.dao;

import open.iot.server.dao.util.SqlDao;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author james mu
 * @date 18-12-21 下午2:21
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("open.iot.server.dao.sql")
@EnableJpaRepositories("open.iot.server.dao.sql")
@EntityScan("open.iot.server.dao.model.sql")
@EnableTransactionManagement
@SqlDao
public class JpaDaoConfig {
}
