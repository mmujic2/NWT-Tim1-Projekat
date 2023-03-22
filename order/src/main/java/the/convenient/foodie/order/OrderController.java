package the.convenient.foodie.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
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
}
