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
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.order.exception.OrderNotFoundException;
import the.convenient.foodie.order.exception.OrderPatchInvalidException;
import the.convenient.foodie.order.model.Order;
import the.convenient.foodie.order.repository.MenuItemRepository;
import the.convenient.foodie.order.repository.OrderRepository;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path="/order")
public class OrderController {
    private final OrderRepository orderRepository;

    private final MenuItemRepository menuItemRepository;

    public OrderController(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

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
        System.out.println(patched);
        return objectMapper.treeToValue(patched, Order.class);
    }

    @PatchMapping(path = "/update/{id}", consumes = "application/json")
    @ResponseBody
    public String UpdateOrderStatus(@PathVariable Long id, @RequestBody JsonNode patch) throws OrderNotFoundException, OrderPatchInvalidException, IOException {
        List<String> allowedFields = List.of("/orderStatus", "/deliveryPersonId");
        for(var p : patch) {
            if(!p.get("path").asText().equals(allowedFields.get(0)) && !p.get("path").asText().equals(allowedFields.get(0))) {
                throw new OrderPatchInvalidException();
            }
        }

        JsonPatch patchClean = JsonPatch.fromJson(patch);

        var order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        try {
            var orderPatched = applyPatchToOrder(patchClean, order);
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

    @GetMapping(path = "/get/{id}")
    public @ResponseBody Order GetOrderById(@PathVariable Long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @DeleteMapping(path = "/delete/{id}")
    public @ResponseBody String DeleteOrderById(@PathVariable Long id) throws OrderNotFoundException {

        orderRepository.delete(orderRepository.findById(id).orElseThrow(OrderNotFoundException::new));
        return "Order successfully deleted!";
    }

    @PostConstruct
    public void init() {
        Order.menuItemRepository = menuItemRepository;
    }
}
