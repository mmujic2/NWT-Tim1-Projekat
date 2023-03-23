package the.convenient.foodie.restaurant.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import the.convenient.foodie.restaurant.util.UUIDGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "restaurant")
public class Restaurant implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", updatable = false,unique = true, nullable = false, columnDefinition = "VARCHAR(60)")
    private String uuid;

    @Column(name = "name")
    @NotNull(message = "Restaurant name must be specified!")
    @Size(min=3,max=80,message = "Restaurant name must be between 3 and 80 characters!")
    private String name;

    @Column(name = "address")
    @Size(min=3,max=80,message = "Restaurant address must be between 3 and 80 characters!")
    private String address;

    @Column(name = "map_coordinates")
    @Pattern(regexp ="^((-)?[0-9]?[0-9]\\.\\d+,(\\s)*(-)?[1]?[0-9]?[0-9]\\.\\d+)",message = "Map coordinates must represent latitude and longitude values!")
    private String mapCoordinates;


    @Column(name = "manager_uuid",columnDefinition = "VARCHAR(60)")
    @NotNull(message="Restaurant manager must be specified!")
    @Size(min=36,max=36,message = "UUID must be 36 characters long!")
    private String managerUUID;

    @Column(name = "logo")
    @Lob
    private byte[] logo;

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


    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="opening_hours")
    private OpeningHours openingHours;

    @ManyToMany
    @JoinTable(name="restaurant_category", joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<Category> categories;
    @PrePersist
    public void initializeUUID() {
        if (uuid == null) {
            uuid = UUIDGenerator.generateType1UUID().toString();
        }
    }

    public Restaurant(Long id, String uuid, String name, String address,
                      String mapCoordinates, String managerUUID, byte[] logo,
                      OpeningHours openingHours, LocalDateTime created, String createdBy, LocalDateTime modified, String modifiedBy, Set<Category> categories) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.address = address;
        this.mapCoordinates = mapCoordinates;
        this.managerUUID = managerUUID;
        this.logo = logo;
        this.openingHours = openingHours;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
        this.categories = categories;
    }

    public Restaurant(String name, String address, String mapCoordinates, String managerUUID, byte[] logo, OpeningHours openingHours, LocalDateTime created, String createdBy) {
        this.name = name;
        this.address = address;
        this.mapCoordinates = mapCoordinates;
        this.managerUUID = managerUUID;
        this.logo = logo;
        this.openingHours = openingHours;
        this.created = created;
        this.createdBy = createdBy;
    }

    public Restaurant() {
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

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
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
