package the.convenient.foodie.menu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import the.convenient.foodie.menu.util.UUIDGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "menu_item")
public class MenuItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Menu item name should not be null")
    @Size(min = 2, max = 30, message = "Menu item name must be between 2 and 30 characters!")
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @Size(max = 100, message = "Menu item description can contain a maximum of 100 characters!")
    private String description;

    @NotNull(message = "Menu item price should not be null")
    @Min(value = 0, message = "Price can not be negative")
    @Column(name = "price")
    private Double price;

    @Column(name = "discount_price")
    @Min(value = 0, message = "Discount price can not be negative")
    private Double discount_price;

    @Column(name = "prep_time")
    @Min(value = 0,message = "Prep time can not be negative")
    private Double prep_time;

    @Column(name = "uuid", updatable = false,unique = true, nullable = false, columnDefinition = "VARCHAR(60)")
    private String uuid;

    @Column(name = "image")
    @Lob
    private byte[] image;

    @Column(name="date_created")
    @NotNull(message = "Creation date must be specified!")
    private LocalDateTime date_created;

    @Column(name="date_modified")
    private LocalDateTime date_modified;


    @PrePersist
    public void initializeUUID() {
        if (uuid == null) {
            uuid = UUIDGenerator.generateType1UUID().toString();
        }
    }

    public MenuItem(Long id, String name, String description, Double price, Double discount_price, Double prep_time, String uuid, byte[] image, LocalDateTime date_created, LocalDateTime date_modified) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount_price = discount_price;
        this.prep_time = prep_time;
        this.uuid = uuid;
        this.image = image;
        this.date_created = date_created;
        this.date_modified = date_modified;
    }

    public MenuItem() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(Double discount_price) {
        this.discount_price = discount_price;
    }

    public Double getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(Double prep_time) {
        this.prep_time = prep_time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public LocalDateTime getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDateTime date_created) {
        this.date_created = date_created;
    }

    public LocalDateTime getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(LocalDateTime date_modified) {
        this.date_modified = date_modified;
    }

}
