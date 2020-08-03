package open.iot.server.dao.model;


public interface ToData<T> {
    /**
     * This method convert domain model object to data transfer object
     *
     * @return the dto object
     */
    T toData();
}
