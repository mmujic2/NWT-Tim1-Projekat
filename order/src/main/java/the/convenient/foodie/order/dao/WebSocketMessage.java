package the.convenient.foodie.order.dao;

public class WebSocketMessage {
    private String message;

    public WebSocketMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
