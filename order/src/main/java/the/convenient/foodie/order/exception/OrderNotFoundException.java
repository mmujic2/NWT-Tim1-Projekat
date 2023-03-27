package the.convenient.foodie.order.exception;

public class OrderNotFoundException extends Exception{
    public OrderNotFoundException() {
        super("Order not found!");
    }
}
