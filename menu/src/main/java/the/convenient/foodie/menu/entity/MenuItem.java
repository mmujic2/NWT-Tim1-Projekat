package the.convenient.foodie.menu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull(message = "Menu item price should not be null")
    @Positive(message = "Price can not be negative")
    @Column(name = "price")
    private Double price;

    @Column(name = "discount_price")
    @Positive(message = "Discount price can not be negative")
    private Double discount_price;

    @Column(name = "prep_time")
    @Positive(message = "Prep time can not be negative")
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

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "menu_id")
    @NotNull(message = "The menu item belongs to the menu")
    Menu menu;

    @PrePersist
    public void initializeUUID() {
        if (uuid == null) {
            uuid = UUIDGenerator.generateType1UUID().toString();
        }
    }

    public MenuItem(Long id, String name, String description, Double price, Double discount_price, Double prep_time, String uuid, byte[] image, LocalDateTime date_created, LocalDateTime date_modified, Menu menu) {
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
        this.menu = menu;
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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
