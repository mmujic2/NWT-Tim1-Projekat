package the.convenient.foodie.restaurant.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class RestaurantUpdateRequest implements Serializable {
    @NotNull(message = "Restaurant name must be specified!")
    @Size(min=3,max=80,message = "Restaurant name must be between 3 and 80 characters!")
    private String name;
    @Size(min=3,max=80,message = "Restaurant address must be between 3 and 80 characters!")
    @NotNull(message = "Restaurant address must be specified!")
    private String address;
    @Pattern(regexp ="^((-)?[0-9]?[0-9]\\.\\d+,(\\s)*(-)?[1]?[0-9]?[0-9]\\.\\d+)",message = "Map coordinates must represent latitude and longitude values!")
    @NotNull(message = "Map coordinates must be specified!")
    private String mapCoordinates;


    @NotNull(message="Restaurant manager must be specified!")
    @Size(min=36,max=36,message = "UUID must be 36 characters long!")
    private String managerUUID;

    public RestaurantUpdateRequest(String name, String address, String mapCoordinates, String managerUUID) {
        this.name = name;
        this.address = address;
        this.mapCoordinates = mapCoordinates;
        this.managerUUID = managerUUID;
    }

    public RestaurantUpdateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMapCoordinates() {
        return mapCoordinates;
    }

    public void setMapCoordinates(String mapCoordinates) {
        this.mapCoordinates = mapCoordinates;
    }

    public String getManagerUUID() {
        return managerUUID;
    }

    public void setManagerUUID(String managerUUID) {
        this.managerUUID = managerUUID;
    }
}
