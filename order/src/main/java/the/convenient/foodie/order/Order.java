package the.convenient.foodie.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Long restaurantId;

    private Integer estimatedDeliveryTime;

    private String createdTime;

    @Nullable
    private Long couponId;

    private String orderStatus;

    private Double totalPrice;

    @Nullable
    private Long deliveryPersonId;

    private Double deliveryFee;

    private String orderCode;

    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "OrderMenuItems",
    joinColumns = { @JoinColumn(name = "order_id")},
    inverseJoinColumns = {@JoinColumn(name = "menuItem_id")})
    private List<MenuItem> menuItems;

    public Order() {

    }

    public Order(Long userId, Long restaurantId, Integer estimatedDeliveryTime, String createdTime, Long couponId, String orderStatus, Double totalPrice, Long deliveryPersonId, Double deliveryFee, String orderCode, ArrayList<MenuItem> menuItems) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.createdTime = createdTime;
        this.couponId = couponId;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.deliveryPersonId = deliveryPersonId;
        this.deliveryFee = deliveryFee;
        this.orderCode = orderCode;
        this.menuItems = menuItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return userId;
    }

    public void setUser_id(Long user_id) {
        this.userId = user_id;
    }

    public Long getRestaurant_id() {
        return restaurantId;
    }

    public void setRestaurant_id(Long restaurant_id) {
        this.restaurantId = restaurant_id;
    }

    public Integer getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(Integer estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public void setDeliveryPersonId(Long deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    /*public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }*/

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
