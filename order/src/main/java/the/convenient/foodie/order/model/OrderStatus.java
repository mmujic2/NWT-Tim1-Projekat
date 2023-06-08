package the.convenient.foodie.order.model;

public enum OrderStatus {
    PENDING("Pending"),
    IN_PREPARATION("In preparation"),
    READY_FOR_DELIVERY("Ready for delivery"),
    ACCEPTED_FOR_DELIVERY("Accepted for delivery"),
    IN_DELIVERY("In delivery"),
    DELIVERED("Delivered"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");


    String name;
    OrderStatus(String name) {

        this.name  = name;
    }

    public String getName() {
        return name;
    }

}
