package the.convenient.foodie.restaurant.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import the.convenient.foodie.restaurant.model.Category;
import the.convenient.foodie.restaurant.model.OpeningHours;
import the.convenient.foodie.restaurant.model.Restaurant;

import java.time.LocalDateTime;
import java.util.Set;

public class RestaurantWithRating {

    private Long id;

    private String uuid;

    private String name;

    private String address;


    private byte[] logo;

    private LocalDateTime created;


    private OpeningHours openingHours;

    Set<Category> categories;
    Double rating;

    public RestaurantWithRating(Restaurant restaurant,Double rating) {
        this.id= restaurant.getId();
        this.name=restaurant.getName();
        this.uuid = restaurant.getUuid();
        this.address=restaurant.getAddress();
        this.logo=restaurant.getLogo();
        this.rating=rating;
        this.categories=restaurant.getCategories();
        this.created=restaurant.getCreated();
        this.openingHours=restaurant.getOpeningHours();
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

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
