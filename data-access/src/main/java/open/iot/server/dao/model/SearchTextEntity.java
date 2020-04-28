package open.iot.server.dao.model;


/**
 * @author james mu
 * @date 18-12-13 下午4:05
 */
public interface SearchTextEntity<D> extends BaseEntity<D> {

    String getSearchTextSource();

    void setSearchText(String searchText);
}
