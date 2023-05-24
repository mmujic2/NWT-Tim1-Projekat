package the.convenient.foodie.restaurant.dto.restaurantimage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RestaurantImageUploadRequest {
    @NotNull(message = "Restaurant id must be specified!")
    private Long restaurantId;

    @NotNull(message = "Image data must not be empty!")
    @NotBlank(message = "Image data must not be empty!")
    private String imageData;

    public RestaurantImageUploadRequest(Long restaurantId, String imageData) {
        this.restaurantId = restaurantId;
        this.imageData = imageData;
    }

    public RestaurantImageUploadRequest() {
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
