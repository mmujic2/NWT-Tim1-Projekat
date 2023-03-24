package the.convenient.foodie.order.exception;

public class MenuItemDoesntExistException extends Exception {
    public MenuItemDoesntExistException(String message) {
        super(message);
    }
}
