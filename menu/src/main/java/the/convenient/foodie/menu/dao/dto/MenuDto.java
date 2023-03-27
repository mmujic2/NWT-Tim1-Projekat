package the.convenient.foodie.menu.dao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class MenuDto implements Serializable {
    @NotNull(message = "Active status should not be null")
    private boolean active;

    public MenuDto() {
    }

    public MenuDto(boolean active, String restaurant_uuid) {
        this.active = active;
        this.restaurant_uuid = restaurant_uuid;
    }

    @NotNull(message="Menu restaurant must be specified!")
    @Size(min=36,max=36,message = "UUID must be 36 characters long!")
    private String restaurant_uuid;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRestaurant_uuid() {
        return restaurant_uuid;
    }

    public void setRestaurant_uuid(String restaurant_uuid) {
        this.restaurant_uuid = restaurant_uuid;
    }
}
