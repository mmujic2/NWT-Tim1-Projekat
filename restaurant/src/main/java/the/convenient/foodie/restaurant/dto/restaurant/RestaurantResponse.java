package the.convenient.foodie.restaurant.dto.restaurant;

import the.convenient.foodie.restaurant.dto.category.CategoryResponse;
import the.convenient.foodie.restaurant.dto.openinghours.OpeningHoursResponse;
import the.convenient.foodie.restaurant.model.Restaurant;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RestaurantResponse {

    private Long id;

    private String uuid;

    private String name;

    private String address;


    private String logo;
    private String managerUuid;

    OpeningHoursResponse openingHours;

    String mapCoordinates;

    List<CategoryResponse> categories;
    Double rating;

    Integer customersRated;
    Integer customersFavorited;

    public RestaurantResponse(Restaurant restaurant, Double rating, Number customersRated, Number customersFavorited) {
        this.id= restaurant.getId();
        this.name=restaurant.getName();
        this.uuid = restaurant.getUuid();
        this.address=restaurant.getAddress();
        this.mapCoordinates = restaurant.getMapCoordinates();
        this.openingHours = new OpeningHoursResponse(restaurant.getOpeningHours());
        this.logo=restaurant.getLogo();
        this.rating=rating;
        this.customersFavorited = customersFavorited.intValue();
        this.customersRated = customersRated.intValue();
        this.categories= restaurant.getCategories().stream().map( c -> new CategoryResponse(c.getId(),c.getName())).collect(Collectors.toList());
        this.managerUuid=restaurant.getManagerUUID();

    }

    public RestaurantResponse(Restaurant restaurant) {
        this.id= restaurant.getId();
        this.name=restaurant.getName();
        this.uuid = restaurant.getUuid();
        this.address=restaurant.getAddress();
        this.mapCoordinates = restaurant.getMapCoordinates();
        this.openingHours = new OpeningHoursResponse(restaurant.getOpeningHours());
        this.logo=restaurant.getLogo();
        this.categories=restaurant.getCategories().stream().map( c -> new CategoryResponse(c.getId(),c.getName())).collect(Collectors.toList());
        this.managerUuid=restaurant.getManagerUUID();

    }

    public RestaurantResponse(Long id, String uuid, String name, String address, String logo, OpeningHoursResponse openingHours, String mapCoordinates, List<CategoryResponse> categories, Double rating, Integer customersRated, Integer customersFavorited) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.address = address;
        this.logo = logo;
        this.openingHours = openingHours;
        this.mapCoordinates = mapCoordinates;
        this.categories = categories;
        this.rating = rating;
        this.customersRated = customersRated;
        this.customersFavorited = customersFavorited;
    }

    public RestaurantResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public OpeningHoursResponse getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHoursResponse openingHours) {
        this.openingHours = openingHours;
    }

    public String getMapCoordinates() {
        return mapCoordinates;
    }

    public void setMapCoordinates(String mapCoordinates) {
        this.mapCoordinates = mapCoordinates;
    }

    public List<CategoryResponse> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryResponse> categories) {
        this.categories = categories;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getCustomersRated() {
        return customersRated;
    }

    public void setCustomersRated(Integer customersRated) {
        this.customersRated = customersRated;
    }

    public Integer getCustomersFavorited() {
        return customersFavorited;
    }

    public void setCustomersFavorited(Integer customersFavorited) {
        this.customersFavorited = customersFavorited;
    }

    public String getManagerUuid() {
        return managerUuid;
    }
    public void setManagerUuid(String managerUuid) {
        this.managerUuid = managerUuid;
    }
}
