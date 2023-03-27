package the.convenient.foodie.order.exception;

public class MenuItemNotFoundException extends Exception {
    public MenuItemNotFoundException() {
        super("Menu item not found!");
    }
}
