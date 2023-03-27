package the.convenient.foodie.discount.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer     id;
    @Column(name = "code", unique = true, columnDefinition = "VARCHAR(60)")
    private String      code;
    @Column(name = "quantity", columnDefinition = "integer")
    private Integer     quantity;
    @Column(name = "restaurant_id", unique = true, columnDefinition = "integer")
    private Integer     restaurant_id;
    @Column(name = "discount_percentage", columnDefinition = "integer")
    private Integer     discount_percentage;
    @Column(name = "coupon_uuid", unique = true, columnDefinition = "VARCHAR(60)")
    private String      coupon_uuid;

    public Coupon() {
        this.code = "";
        this.quantity = 0;
        this.restaurant_id = null;
        this.discount_percentage = 0;
        this.coupon_uuid = "";
    }

    public Coupon(String code, Integer quantity, Integer restaurant_id, Integer discount_percentage, String coupon_uuid) {
        this.code = code;
        this.quantity = quantity;
        this.restaurant_id = restaurant_id;
        this.discount_percentage = discount_percentage;
        this.coupon_uuid = coupon_uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Integer restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public Integer getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(Integer discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getCoupon_uuid() {
        return coupon_uuid;
    }

    public void setCoupon_uuid(String coupon_uuid) {
        this.coupon_uuid = coupon_uuid;
    }
}
