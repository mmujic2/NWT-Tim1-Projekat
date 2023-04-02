package the.convenient.foodie.discount.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import the.convenient.foodie.discount.dto.CouponDto;
import the.convenient.foodie.discount.util.UUIDGenerator;

@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer     id;

    @Size(min = 12, max = 12, message = "Coupon code must be 12 characters long!")
    @NotNull(message = "Coupon code should not be null")
    @Column(name = "code", unique = true, columnDefinition = "VARCHAR(60)")
    private String      code;

    @NotNull(message = "Quantity should not be null")
    @Positive(message = "Quantity can not be negative")
    @Column(name = "quantity", columnDefinition = "integer")
    private Integer     quantity;

    @NotNull(message = "Restaurant ID should not be null")
    @Column(name = "restaurant_id", unique = true, columnDefinition = "integer")
    private Integer     restaurant_id;

    @NotNull(message = "Discount percentage should not be null")
    @Positive(message = "Discount percentage can not be negative")
    @Column(name = "discount_percentage", columnDefinition = "integer")
    private Integer     discount_percentage;

    //@NotNull(message="Coupon UUID must be specified!")
    @Size(min=36,max=36,message = "UUID must be 36 characters long!")
    @Column(name = "coupon_uuid", unique = true, columnDefinition = "VARCHAR(60)")
    private String      coupon_uuid;

    @PrePersist
    public void initializeUUID() {
        if (coupon_uuid == null) {
            coupon_uuid = UUIDGenerator.generateType1UUID().toString();
        }
    }

    public Coupon() {
        this.code = "";
        this.quantity = 0;
        this.restaurant_id = null;
        this.discount_percentage = 0;
    }

    public Coupon(String code, Integer quantity, Integer restaurant_id, Integer discount_percentage) {
        this.code = code;
        this.quantity = quantity;
        this.restaurant_id = restaurant_id;
        this.discount_percentage = discount_percentage;
    }

    public Coupon(CouponDto couponDto){
        this.code = couponDto.getCode();
        this.quantity = couponDto.getQuantity();
        this.restaurant_id = couponDto.getRestaurant_id();
        this.discount_percentage = couponDto.getDiscount_percentage();
        this.coupon_uuid = couponDto.getCoupon_uuid();
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
