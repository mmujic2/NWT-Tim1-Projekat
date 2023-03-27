package the.convenient.foodie.menu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "menu")
public class Menu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "restaurant_uuid", columnDefinition = "VARCHAR(60)")
    @NotNull(message="Menu restaurant must be specified!")
    @Size(min=36,max=36,message = "UUID must be 36 characters long!")
    private String restaurant_uuid;

    @Column(name = "active")
    @NotNull(message = "Active status should not be null")
    private boolean active;

    @Column(name="date_created")
    @NotNull(message = "Creation date must be specified!")
    private LocalDateTime date_created;

    @Column(name="date_modified")
    private LocalDateTime date_modified;

    public Menu() {
    }

    public Menu(Long id, String restaurant_uuid, boolean active, LocalDateTime date_created, LocalDateTime date_modified) {
        this.id = id;
        this.restaurant_uuid = restaurant_uuid;
        this.active = active;
        this.date_created = date_created;
        this.date_modified = date_modified;
    }

    public Menu(Long id, String restaurant_uuid, boolean active) {
        this.id = id;
        this.restaurant_uuid = restaurant_uuid;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(LocalDateTime date_modified) {
        this.date_modified = date_modified;
    }

    public LocalDateTime getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDateTime date_created) {
        this.date_created = date_created;
    }

    public String getRestaurant_uuid() {
        return restaurant_uuid;
    }

    public void setRestaurant_uuid(String restaurant_uuid) {
        this.restaurant_uuid = restaurant_uuid;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
