package open.iot.server.dao.sql;

import open.iot.server.dao.model.BaseEntity;
import open.iot.server.dao.model.SearchTextEntity;


public abstract class JpaAbstractSearchTextDao <E extends BaseEntity<D>,D> extends JpaAbstractDao<E,D> {

    @Override
    protected void setSearchText(E entity){
        ((SearchTextEntity) entity).setSearchText(((SearchTextEntity) entity).getSearchTextSource().toLowerCase());
    }

}
