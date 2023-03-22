package the.convenient.foodie.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;

@RestController
@RequestMapping(path="/order")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewOrder(@RequestParam Long userId,
                                            @RequestParam Long retsaurantId,
                                            @RequestParam Integer estDelTime,
                                            @RequestParam String createdTime,
                                            @RequestParam Long couponId,
                                            @RequestParam String orderStatus,
                                            @RequestParam Double totalPrice,
                                            @RequestParam Long deliveryPersonId,
                                            @RequestParam Double deliveryFee,
                                            @RequestParam String orderCode,
                                            @RequestParam ArrayList<Long> menuItemIds
                                            ) {
        var menuItems = new ArrayList<MenuItem>();
        menuItemRepository.findAllById(menuItemIds).forEach(menuItems::add);

        orderRepository.save(new Order(userId, retsaurantId, estDelTime, createdTime, couponId, orderStatus, totalPrice,
                deliveryPersonId, deliveryFee, orderCode, menuItems));

        return "Ok";
    }

    @PutMapping(path = "/updatecourier")
    public @ResponseBody String UpdateOrderCourier(@RequestParam Long orderid, @RequestParam Long courierid) {
        var order = orderRepository.findById(orderid);
        if(order.isPresent()) {
            try {
                ;order.get().setDeliveryPersonId(courierid);
                orderRepository.save(order.get());
                return "Order" + orderid + " courier changed to " + courierid;
            }
            catch(Exception e) {
                return "Error while changing order courier";
            }
        }
        else {
            return "Error: Order not found";
        }
    }

    @PutMapping(path = "/updatestatus")
    public @ResponseBody String UpdateOrderStatus(@RequestParam Long orderid, @RequestParam String status) {
        var order = orderRepository.findById(orderid);
        if(order.isPresent()) {
            try {
                order.get().setOrderStatus(status);
                orderRepository.save(order.get());

                return "Order " + orderid + " status changed to " + status;
            }
            catch(Exception e) {
                return "Error while changing order status";
            }
        }
        else {
            return "Error: Order not found";
        }
    }

    @GetMapping(path = "/get")
    public @ResponseBody Iterable<Order> GetAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping(path = "/getid")
    public @ResponseBody Order GetOrderById(@RequestParam Long id) {
        return orderRepository.findById(id).get();
    }
}
