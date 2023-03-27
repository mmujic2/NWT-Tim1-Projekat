package the.convenient.foodie.order.exception;

public class OrderPatchInvalidException extends Exception {
    public OrderPatchInvalidException () {
        super("Only deliveryPersonId and orderStatus fields can be updated!");
    }
}
