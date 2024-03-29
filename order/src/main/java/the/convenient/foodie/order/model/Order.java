package the.convenient.foodie.order.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.Iterables;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    private String userId;

    @NotNull(message = "Order must have a restaurant!")
    private String restaurantId;

    @NotNull(message = "Estimated time must be set!")
    private Integer estimatedDeliveryTime;

    @NotNull(message = "Creation time must be set!")
    private LocalDateTime createdTime;

    @Nullable
    private String couponId;

    @NotNull(message = "Order must have status!")
    private String orderStatus;

    @NotNull(message = "Order must have price!")
    @PositiveOrZero(message = "Price must be positive!")
    private Double totalPrice;

    @Nullable
    private String deliveryPersonId;

    @NotNull(message = "Order must have delivery fee!")
    @PositiveOrZero(message = "Delivery fee must be positive!")
    private Double deliveryFee;

    // @NotNull(message = "Order must have code")
    @Nullable
    private String orderCode;

    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.MERGE
    })
    @JoinTable(name = "OrderMenuItems",
    joinColumns = { @JoinColumn(name = "order_id")},
    inverseJoinColumns = {@JoinColumn(name = "menuItem_id")})
    @NotNull(message = "Order must contain at least one menu item!")
    private List<MenuItem> menuItems;

    private String restaurantName;

    private String customerPhoneNumber;

    private String customerAddress;

    private String restaurantAddress;


    /*@Transient
    @NotNull(message = "Order must contain at least one menu item!")
    @Size(min = 1, message = "Order must contain at least one menu item!")
    private ArrayList<Long> menuItemIds;*/

    public Order() {

    }

    @JsonCreator
    public Order(String userId, String restaurantId, Integer estimatedDeliveryTime, LocalDateTime createdTime, String couponId, String orderStatus, Double totalPrice, String deliveryPersonId, Double deliveryFee, String orderCode, ArrayList<Long> menuItemIds) throws MenuItemNotFoundException {
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

    public Order(String userId, String restaurantId, Integer estimatedDeliveryTime, LocalDateTime createdTime, String couponId, String orderStatus, Double totalPrice, String deliveryPersonId, Double deliveryFee, String orderCode, List<MenuItem> menuItems, String restaurantName, String customerPhoneNumber, String customerAddress, String restaurantAddress) {
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
        this.restaurantName = restaurantName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerAddress = customerAddress;
        this.restaurantAddress = restaurantAddress;
    }


    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser_id() {
        return userId;
    }

    public void setUser_id(String user_id) {
        this.userId = user_id;
    }

    public String getRestaurant_id() {
        return restaurantId;
    }

    public void setRestaurant_id(String restaurant_id) {
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

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public void setDeliveryPersonId(String deliveryPersonId) {
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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String restaurantPhoneNumber) {
        this.customerPhoneNumber = restaurantPhoneNumber;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }


}
