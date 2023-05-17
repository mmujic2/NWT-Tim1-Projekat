package the.convenient.foodie.order.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Entity
@Table(name = "MenuItems")
public class MenuItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Order must contain a name!")
    private String name;

    @Nullable
    private String description;

    @NotNull(message = "Order must have a price!")
    private Double price;

    private Double discount_price;

    @Column(name = "uuid", updatable = false,unique = true, nullable = false, columnDefinition = "VARCHAR(60)")
    private String uuid;

    @Lob
    @Nullable
    private byte[] image;

    @Column(name="date_created")
    private LocalDateTime date_created;

    @Column(name="date_modified")
    private LocalDateTime date_modified;

    @NotNull(message = "Menu item must have preparation time!")
    @Positive(message = "Preparation time must be positive number!")
    private Double prep_time;

    public MenuItem() {

    }

    @JsonCreator
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

    /*@JsonCreator
    public MenuItem(String name, String description, Double price, Double discountPrice, Byte[] image, Integer preparationTime) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount_Price = discountPrice;
        this.image = image;
        this.preparationTime = preparationTime;
    }*/

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

    public Double getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(Double prep_time) {
        this.prep_time = prep_time;
    }
}
