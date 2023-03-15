package the.convenient.foodie.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/order")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping(path="/all")
    public @ResponseBody Iterable<OrderMenuItems> getAllMenuItems() {
        return orderRepository.findAll();
    }
}
