package the.convenient.foodie.order.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Iterables;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import the.convenient.foodie.order.repository.MenuItemRepository;
import the.convenient.foodie.order.exception.MenuItemNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order {
    public static MenuItemRepository menuItemRepository;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Order must have a user!")
    private Long userId;

    @NotNull(message = "Order must have a restaurant!")
    private Long restaurantId;

    @NotNull(message = "Estimated time must be set!")
    private Integer estimatedDeliveryTime;

    @NotNull(message = "Creation time must be set!")
    private LocalDateTime createdTime;

    @Nullable
    private Long couponId;

    @NotNull(message = "Order must have status!")
    private String orderStatus;

    @NotNull(message = "Order must have price!")
    @Positive(message = "Price must be positive!")
    private Double totalPrice;

    @Nullable
    private Long deliveryPersonId;

    @NotNull(message = "Order must have delivery fee!")
    @Positive(message = "Delivery fee must be positive!")
    private Double deliveryFee;

    // @NotNull(message = "Order must have code")
    private String orderCode;

    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "OrderMenuItems",
    joinColumns = { @JoinColumn(name = "order_id")},
    inverseJoinColumns = {@JoinColumn(name = "menuItem_id")})
    @NotNull(message = "Order must contain at least one menu item!")
    @Size(min = 1, message = "Order must contain at least one menu item!")
    private List<MenuItem> menuItems;

    /*@Transient
    @NotNull(message = "Order must contain at least one menu item!")
    @Size(min = 1, message = "Order must contain at least one menu item!")
    private ArrayList<Long> menuItemIds;*/

    public Order() {

    }

    @JsonCreator
    public Order(Long userId, Long restaurantId, Integer estimatedDeliveryTime, LocalDateTime createdTime, Long couponId, String orderStatus, Double totalPrice, Long deliveryPersonId, Double deliveryFee, String orderCode, ArrayList<Long> menuItemIds) throws MenuItemNotFoundException {
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
        if(menuItemIds != null) {
            var menuItems = menuItemRepository.findAllById(menuItemIds);
            if(Iterables.size(menuItems) != menuItemIds.size()) {
                throw new MenuItemNotFoundException();
            }
            this.menuItems = new ArrayList<>();
            this.menuItems.addAll(menuItems);
        }
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
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

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
