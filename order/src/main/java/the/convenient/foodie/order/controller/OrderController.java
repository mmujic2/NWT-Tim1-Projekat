package the.convenient.foodie.order.controller;

import com.example.demo.EventRequest;
import com.example.demo.EventServiceGrpc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import the.convenient.foodie.order.dao.*;
import the.convenient.foodie.order.exception.OrderNotFoundException;
import the.convenient.foodie.order.exception.OrderPatchInvalidException;
import the.convenient.foodie.order.model.MenuItem;
import the.convenient.foodie.order.model.MenuItemDTO;
import the.convenient.foodie.order.model.Order;
import the.convenient.foodie.order.model.OrderStatus;
import the.convenient.foodie.order.repository.MenuItemRepository;
import the.convenient.foodie.order.repository.OrderRepository;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path="/order")
public class OrderController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    public RestTemplate restTemplate;

    private final OrderRepository orderRepository;

    private final MenuItemRepository menuItemRepository;

    private final List<Character> chars = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '#', '$', '%', '&', '/', '(', ')', '=', '?', '*');

    public OrderController(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse( responseCode = "201", description = "Successfully created a new order",
                content = {@Content(mediaType = "application/json",
                           schema = @Schema(implementation = Order.class))}),
            @ApiResponse( responseCode = "400", description = "Invalid information supplied", content = @Content)
    })
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
     public @ResponseBody ResponseEntity<Order> addNewOrder(@RequestBody OrderCreateRequest orderCreateRequest,
                                                            @RequestHeader("uuid") String userUUID) throws JsonProcessingException {
        Random rand = new Random();
        StringBuilder code = new StringBuilder();
        // sansa da se generisu dva ista koda je 1e-19
        for(int i = 0; i < 10; i++) {
            code.append(chars.get(rand.nextInt(chars.size())));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.registerModule(new ParameterNamesModule());

        List<MenuItem> menuItems = new ArrayList<>();
        List<Long> processedIds = new ArrayList<>();
        for(var id : orderCreateRequest.getMenuItemIds()) {
            var newMenuItem = menuItemRepository.findById(id);
            if(newMenuItem.isPresent()) {
                menuItems.add(newMenuItem.get());
            }
            else {
                MenuItem temp = null;
                var menuItemXml = restTemplate.getForObject("http://menu-service/menu-item/get/" + id.toString(), String.class);
                temp = xmlMapper.readValue(menuItemXml, MenuItem.class);
                temp.setImage(null);
                menuItems.add(temp);
                if(!processedIds.contains(id)) {
                    menuItemRepository.save(temp);
                }
            }
            processedIds.add(id);
        }

        System.out.println(menuItems);

        var order = new Order(userUUID,
                orderCreateRequest.getRestaurantId(),
                orderCreateRequest.getEstimatedDeliveryTime(),
                LocalDateTime.now(),
                orderCreateRequest.getCouponId(),
                OrderStatus.PENDING.getName(),
                orderCreateRequest.getTotalPrice(),
                null,
                orderCreateRequest.getDeliveryFee(),
                code.toString(),
                menuItems,
                orderCreateRequest.getRestaurantName(),
                orderCreateRequest.getCustomerPhoneNumber(),
                orderCreateRequest.getCustomerAddress(),
                orderCreateRequest.getRestaurantAddress());

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

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(description = "Get all orders")
    @ApiResponses(value = {
            @ApiResponse( responseCode = "200", description = "Successfully found all orders",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))})
    })
    @GetMapping(path = "/get")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<Order> GetAllOrders(@RequestHeader("username") String username) {
        //var x = restTemplate.getForObject("http://discount-service/coupon/all", String.class);
        //System.out.println(x);
        // rabbitTemplate.convertAndSend("", "this is a message");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        EventServiceGrpc.EventServiceBlockingStub stub = EventServiceGrpc.newBlockingStub(channel);
        stub.logevent(EventRequest
                .newBuilder()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction("GET")
                .setEvent("Fetched all orders").setServiceName("order-service")
                .setUser(username)
                .build());

        return orderRepository.findAll();
    }

    @RabbitListener(queues = "menuItemCreate")
    public void listen(String menuItemsJson) {
        System.out.println(menuItemsJson);
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());

        try {
            objectMapper.readValue(menuItemsJson, MenuItem[].class);
            List<MenuItem> menuItemsList = objectMapper.readValue(menuItemsJson, new TypeReference<>() {});
            for(var item : menuItemsList) {
                menuItemRepository.save(item);
                item.setImage(null);
            }
            System.out.println("Done");
            System.out.println(menuItemsList.get(0));
        } catch (Exception e) {
            rabbitTemplate.convertAndSend("menuItemCreateError", menuItemsJson);
        }
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


    @PostMapping(path = "/count/{sorttype}")
    public @ResponseBody Map<String, Long> GetRestaurantOrderCounts(@RequestBody List<String> restaurantUids, @PathVariable String sorttype) {
        var orders = orderRepository.findAll();

        Map<String, Long> orderMap = new HashMap<>();
        for(var id : restaurantUids) {
            orderMap.put(id, orders.stream().filter(x -> x.getRestaurant_id().equals(id)).count());
        }

        if(sorttype.equals("desc")) {
            return orderMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        }
        else if(sorttype.equals("asc")) {
            return orderMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        }

        return orderMap;
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/adminorders")
    public Map<String, Long> getAdminOrders(){
        Map<String, Long> ordersMap = new HashMap<>();
        ArrayList<String> names = new ArrayList<>();
        var allOrders = orderRepository.findAll();
        allOrders.forEach(order -> {
            if(!names.contains(order.getRestaurantName())) names.add(order.getRestaurantName());
        });

        for(var name : names) {
            ordersMap.put(name, allOrders.stream().filter(x -> x.getRestaurantName().equals(name)).count());
        }
        System.out.println("Odradio sam sve");
        System.out.println(allOrders);
        return ordersMap;
    }
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/adminspending")
    public Long getAdminSpending(){
        long moneySpent;
        var allOrders = orderRepository.findAll();
        moneySpent = allOrders.stream().mapToLong(order -> (long) order.getTotalPrice()).sum();
        System.out.println("Odradio sam sve");
        System.out.println(allOrders.size());
        return moneySpent;
    }
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/adminrestaurantrevenue")
    public Map<String, Long> getAdminRestaurantRevenue(){
        Map<String, Long> ordersMap = new HashMap<>();
        ArrayList<String> names = new ArrayList<>();
        var allOrders = orderRepository.findAll();
        allOrders.forEach(order -> {
            if(!names.contains(order.getRestaurantName())) names.add(order.getRestaurantName());
        });

        for(var name : names) {
            ArrayList<Order> listOrders = (ArrayList<Order>) allOrders.stream().filter(x -> x.getRestaurantName().equals(name)).collect(Collectors.toList());
            long  money = listOrders.stream().mapToLong(order -> (long) order.getTotalPrice()).sum();
            ordersMap.put(name, money );
        }
        System.out.println("Odradio sam sve");
        System.out.println(allOrders);
        return ordersMap;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/getforuser")
    public ResponseEntity<List<OrderResponse>> getAllUserOrders(@RequestHeader("uuid") String userUuid) {
        /*return ResponseEntity.ok(orderRepository.findAll().stream()
                .filter(x -> x.getUser_id().equals(userUuid))
                .map(OrderResponse::new)
                .toList());*/
        return ResponseEntity.ok(orderRepository.getOrdersByUserUUID(userUuid).stream().map(OrderResponse::new).toList());
    }

    @PreAuthorize("hasAnyRole('COURIER','RESTAURANT_MANAGER','CUSTOMER')")
    @PutMapping("/status/{id}/{status}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Long id ,@PathVariable String status) throws JsonProcessingException {
        var order = orderRepository.findById(id).orElseThrow();
        switch(status) {
            case ("In preparation"):
                if(!order.getOrderStatus().equals(OrderStatus.PENDING.getName()))
                    return new ResponseEntity<>("Order is not pending!",HttpStatus.BAD_REQUEST);
            case ("Rejected"):
                if(!order.getOrderStatus().equals(OrderStatus.PENDING.getName()))
                    return new ResponseEntity<>("Order is not pending!",HttpStatus.BAD_REQUEST);
                break;
            case ("Cancelled"):
                if(!order.getOrderStatus().equals(OrderStatus.PENDING.getName()))
                    return new ResponseEntity<>("Order is not pending!",HttpStatus.BAD_REQUEST);
                break;
            case ("Accepted for delivery"):
                if(!order.getOrderStatus().equals(OrderStatus.READY_FOR_DELIVERY.getName()))
                    return new ResponseEntity<>("Order is not ready for delivery!",HttpStatus.BAD_REQUEST);
                break;
        }
        order.setOrderStatus(status);
        orderRepository.save(order);

        SendWebSocketMessage(order.getUser_id(), "Order " + order.getOrderCode() + " status changed to: " + status, status);
        return ResponseEntity.ok(new OrderResponse(order));
    }

    @PreAuthorize("hasRole('COURIER')")
    @PutMapping("/adddeliveryperson/{id}")
    public ResponseEntity<OrderResponse> addDeliveryPersonToOrder(@PathVariable Long id, @RequestHeader("uuid") String uuid, @RequestHeader("username") String username) {
        var order = orderRepository.findById(id).orElseThrow();
        order.setDeliveryPersonId(uuid);
        order.setOrderStatus("In delivery");
        orderRepository.save(order);

        SendWebSocketMessage(order.getUser_id(), "Courier " + username + " will pick up order " + order.getOrderCode()+"!", "In delivery" );
        return ResponseEntity.ok(new OrderResponse(order));
    }

    public void SendWebSocketMessage(String uuid, String message, String status) {
        try {
            System.out.println(new ObjectMapper().writeValueAsString(new WebSocketMessage(message, status)));
            restTemplate.postForObject("http://websocket-service/websocket/message/" + uuid,
                    new ObjectMapper().writeValueAsString(new WebSocketMessage(message, status)), String.class);
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasRole('COURIER')")
    @GetMapping("/get/readyfordelivery")
    public ResponseEntity<List<OrderResponse>> getReadyForDeliveryOrders() {
        return ResponseEntity.ok(orderRepository.getReadyForDeliveryOrders().stream().map(OrderResponse::new).toList());
    }

    @PreAuthorize("hasRole('COURIER')")
    @GetMapping("/get/deliveryperson")
    public ResponseEntity<List<OrderResponse>> getOrdersByDeliveryPersonId(@RequestHeader("uuid") String uuid) {
        return ResponseEntity.ok(orderRepository.getOrdersByDeliveryPersonId(uuid).stream().map(OrderResponse::new).toList());
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @GetMapping("/get/restaurant/{uuid}/pending")
    public ResponseEntity<List<OrderResponse>> getPendingOrdersForRestaurant(@PathVariable("uuid") String uuid) {
        return ResponseEntity.ok(orderRepository.getPendingOrdersByRestaurantId(uuid).stream().map(OrderResponse::new).toList());
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @GetMapping("/get/restaurant/{uuid}/in-preparation")
    public ResponseEntity<List<OrderResponse>> getInPreparationOrdersForRestaurant(@PathVariable("uuid") String uuid) {
        return ResponseEntity.ok(orderRepository.getInPreparationOrdersByRestaurantId(uuid).stream().map(OrderResponse::new).toList());
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @GetMapping("/get/restaurant/{uuid}/ready-for-delivery")
    public ResponseEntity<List<OrderResponse>> getReadyForDeliveryOrdersForRestaurant(@PathVariable("uuid") String uuid) {
        return ResponseEntity.ok(orderRepository.getReadyForDeliveryOrdersByRestaurantId(uuid).stream().map(OrderResponse::new).toList());
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @GetMapping("/get/restaurant/{uuid}/delivered")
    public ResponseEntity<List<OrderResponse>> getDeliveredOrdersForRestaurant(@PathVariable("uuid") String uuid) {
        return ResponseEntity.ok(orderRepository.getDeliveredOrdersByRestaurantId(uuid).stream().map(OrderResponse::new).toList());
    }

    @PostConstruct
    public void init() {
        Order.menuItemRepository = menuItemRepository;
    }
}
