package open.iot.server.common.data;

import open.iot.server.common.data.id.CustomerId;


public class ShortCustomerInfo {

    private CustomerId customerId;

    private String title;

    private boolean isPublic;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShortCustomerInfo that = (ShortCustomerInfo) o;

        return customerId.equals(that.customerId);

    }

    @Override
    public int hashCode() {
        return customerId.hashCode();
    }

    public ShortCustomerInfo(CustomerId customerId, String title, boolean isPublic) {
        this.customerId = customerId;
        this.title = title;
        this.isPublic = isPublic;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
