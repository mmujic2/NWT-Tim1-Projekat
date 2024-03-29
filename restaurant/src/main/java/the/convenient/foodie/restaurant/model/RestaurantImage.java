package the.convenient.foodie.restaurant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="restaurant_image")
public class RestaurantImage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name="image")
    @Lob
    @NotNull(message = "Image data must be defined!")
    private String image;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="restaurant_id")
    @NotNull(message = "The image must be tied to a restaurant!")
    Restaurant restaurant;

    @Column(name="created")
    @NotNull(message = "Creation date must be specified!")
    private LocalDateTime created;

    @Column(name="created_by")
    @NotNull(message ="User that created the record must be specified!")
    private String createdBy;

    @Column(name="modified")
    private LocalDateTime modified;
    @Column(name="modified_by")
    private String modifiedBy;

    public RestaurantImage(Long id, String image, Restaurant restaurant, LocalDateTime created, String createdBy, LocalDateTime modified, String modifiedBy) {
        this.id = id;
        this.image = image;
        this.restaurant = restaurant;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
    }

    public RestaurantImage(@NotNull(message = "Image data must be defined!") String image, Restaurant restaurant, LocalDateTime created, String createdBy) {
        this.image = image;
        this.restaurant = restaurant;
        this.created = created;
        this.createdBy = createdBy;
    }

    public RestaurantImage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}

