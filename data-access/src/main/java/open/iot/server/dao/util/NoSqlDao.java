package open.iot.server.dao.util;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;


@ConditionalOnProperty(prefix = "database", value = "type", havingValue = "mongodb")
public @interface NoSqlDao {
}
