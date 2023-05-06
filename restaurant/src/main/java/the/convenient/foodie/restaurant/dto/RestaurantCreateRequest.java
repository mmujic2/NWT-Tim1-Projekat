package the.convenient.foodie.restaurant.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class RestaurantCreateRequest implements Serializable {
    @NotNull(message = "Restaurant name must be specified!")
    @Size(min=3,max=80,message = "Restaurant name must be between 3 and 80 characters!")
    private String name;


    private String managerUUID;

    public RestaurantCreateRequest(String name, String managerUUID) {
        this.name = name;
        this.managerUUID = managerUUID;
    }

    public RestaurantCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagerUUID() {
        return managerUUID;
    }

    public void setManagerUUID(String managerUUID) {
        this.managerUUID = managerUUID;
    }
}
