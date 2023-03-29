package the.convenient.foodie.restaurant.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="category")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Long id;


    @Size(min=3,max=80,message="Category name must be between 3 and 80 characters!")
    @NotNull(message = "Category name must be defined!")
    @Column(name="name")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    Set<Restaurant> restaurants;

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


    public Category(Long id, String name, LocalDateTime created, String createdBy, LocalDateTime modified, String modifiedBy) {
        this.id = id;
        this.name = name;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
    }

    public Category(String name,LocalDateTime created, String createdBy) {
        this.name = name;
        this.created = created;
        this.createdBy = createdBy;
    }

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(Set<Restaurant> restaurants) {
        this.restaurants = restaurants;
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
