package the.convenient.foodie.restaurant.dto.restaurantimage;


public class RestaurantImageResponse {

    private String imageData;

    public RestaurantImageResponse(String imageData) {
        this.imageData = imageData;
    }

    public RestaurantImageResponse() {
    }



    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
