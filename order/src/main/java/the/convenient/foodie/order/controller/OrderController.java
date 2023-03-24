package the.convenient.foodie.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.order.model.Order;
import the.convenient.foodie.order.repository.MenuItemRepository;
import the.convenient.foodie.order.repository.OrderRepository;

@RestController
@RequestMapping(path="/order")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;


    @PostMapping(path = "/add")
     public @ResponseBody String addNewOrder(@Valid @RequestBody Order order) {
        orderRepository.save(order);

        return "Order successfully created";
    }

    private Order applyPatchToOrder(JsonPatch patch, Order targetOrder) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());

        JsonNode patched = patch.apply(objectMapper.convertValue(targetOrder, JsonNode.class));
        return objectMapper.treeToValue(patched, Order.class);
    }

    @PatchMapping(path = "/update/{id}", consumes = "application/json")
    @ResponseBody
    public String UpdateOrderStatus(@PathVariable Long id, @RequestBody JsonPatch patch) {
        var order = orderRepository.findById(id).orElseThrow(RuntimeException::new);
        try {
            var orderPatched = applyPatchToOrder(patch, order);
            orderRepository.save(orderPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return "Order successfully updated!";
    }

    @GetMapping(path = "/get")
    public @ResponseBody Iterable<Order> GetAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping(path = "/getid")
    public @ResponseBody Order GetOrderById(@RequestParam Long id) {
        return orderRepository.findById(id).get();
    }

    @PostConstruct
    public void init() {
        Order.menuItemRepository = menuItemRepository;
    }
}
