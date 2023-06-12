package the.convenient.foodie.order.dao;

public class AdminOrdersResponse {

    private String restaurantName;

    private Integer numberOfOrders;

    public AdminOrdersResponse(String restaurantName, Integer numberOfOrders) {
        this.restaurantName = restaurantName;
        this.numberOfOrders = numberOfOrders;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Integer getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(Integer numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }
}
