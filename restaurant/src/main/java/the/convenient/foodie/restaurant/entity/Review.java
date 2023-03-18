package the.convenient.foodie.restaurant.entity;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="review")
public class Review implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="comment")
    @Size(min=2,max=100, message = "Comment must be between 2 and 100 characters!")
    private String comment;


    @Min(value=1,message="User rating must be an integer value between 1 and 5!")
    @Max(value=5,message="User rating must be an integer value between 1 and 5!")
    @Column(name="rating")
    private Integer rating;

    @Column(name="user_uuid")
    @NotNull(message = "User must be specified!")
    private String userUUID;

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

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="restaurant_id")
    @NotNull(message = "Restaurant must be specified!")
    private Restaurant restaurant;

    public Review(Long id, String comment, Integer rating, String userUUID, LocalDateTime created, String createdBy, LocalDateTime modified, String modifiedBy, Restaurant restaurant) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.userUUID = userUUID;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
        this.restaurant = restaurant;
    }

    public Review(String comment, Integer rating, String userUUID,Restaurant restaurant, LocalDateTime created, String createdBy) {
        this.comment = comment;
        this.rating = rating;
        this.userUUID = userUUID;
        this.created = created;
        this.createdBy = createdBy;
        this.restaurant = restaurant;
    }

    public Review() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
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
