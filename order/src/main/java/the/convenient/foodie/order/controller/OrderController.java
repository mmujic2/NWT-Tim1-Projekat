package the.convenient.foodie.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(description = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse( responseCode = "201", description = "Successfully created a new order",
                content = {@Content(mediaType = "application/json",
                           schema = @Schema(implementation = Order.class))}),
            @ApiResponse( responseCode = "400", description = "Invalid information supplied", content = @Content)
    })
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
     public @ResponseBody ResponseEntity<Order> addNewOrder(@Valid @RequestBody Order order) {
        orderRepository.save(order);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    private Order applyPatchToOrder(JsonPatch patch, Order targetOrder) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());

        JsonNode patched = patch.apply(objectMapper.convertValue(targetOrder, JsonNode.class));
        System.out.println(patched);
        return objectMapper.treeToValue(patched, Order.class);
    }

    @Operation(description = "Update order by id")
    @ApiResponses(value = {
            @ApiResponse( responseCode = "200", description = "Successfully patched order",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))}),
            @ApiResponse( responseCode = "304", description = "Invalid patch path", content = @Content),
            @ApiResponse( responseCode = "400", description = "Invalid patch", content = @Content),
            @ApiResponse( responseCode = "404", description = "Order not found", content = @Content)
    })
    @PatchMapping(path = "/update/{id}", consumes = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Order> UpdateOrderStatus(@PathVariable Long id, @RequestBody JsonNode patch) throws OrderNotFoundException, OrderPatchInvalidException, IOException {
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
            return new ResponseEntity<>(orderPatched, HttpStatus.OK);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(description = "Get all orders")
    @ApiResponses(value = {
            @ApiResponse( responseCode = "200", description = "Successfully found all orders",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))})
    })
    @GetMapping(path = "/get")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<Order> GetAllOrders() {
        return orderRepository.findAll();
    }

    @Operation(description = "Get all orders")
    @ApiResponses(value = {
            @ApiResponse( responseCode = "200", description = "Successfully found order",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))}),
            @ApiResponse( responseCode = "404", description = "Order with provided ID not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/get/{id}")
    public @ResponseBody Order GetOrderById(@PathVariable Long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @Operation(description = "Delete an order")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted order with provided ID"),
            @ApiResponse(responseCode = "404", description = "Order with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path = "/delete/{id}")
    public @ResponseBody ResponseEntity<String> DeleteOrderById(@PathVariable Long id) throws OrderNotFoundException {

        var order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        orderRepository.delete(order);
        return new ResponseEntity<>("Order successfully deleted", HttpStatus.OK);
    }

    @PostConstruct
    public void init() {
        Order.menuItemRepository = menuItemRepository;
    }
}
