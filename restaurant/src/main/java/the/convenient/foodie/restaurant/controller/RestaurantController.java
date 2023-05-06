package the.convenient.foodie.restaurant.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.example.demo.EventServiceGrpc;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.restaurant.dto.*;
import the.convenient.foodie.restaurant.model.FavoriteRestaurant;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.service.CategoryService;
import the.convenient.foodie.restaurant.service.FavoriteRestaurantService;
import the.convenient.foodie.restaurant.service.RestaurantService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@RestController
@RequestMapping(path="/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private FavoriteRestaurantService favoriteRestaurantService;



    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Create a new restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new restaurant",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Restaurant> addNewRestaurant (
            @Parameter(description = "Information required for restaurant creation", required = true)
            @Valid @RequestBody RestaurantCreateRequest request) {

        var restaurant = restaurantService.addNewRestaurant(request);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        EventServiceGrpc.EventServiceBlockingStub stub = EventServiceGrpc.newBlockingStub(channel);
        var response = stub.logevent(com.example.demo.EventRequest
                .newBuilder()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction("POST")
                .setEvent("Created restaurant " + request.getName()).setServiceName("restaurant-service")
                .setUser("Test")
                .build());


        return new ResponseEntity<>(restaurant,HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Update restaurant information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated restaurant information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path="/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Restaurant> updateRestaurant (
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Restaurant information to be updated", required = true)
            @RequestBody @Valid RestaurantUpdateRequest request) {

        Restaurant restaurant = null;
        restaurant = restaurantService.updateRestaurant(request,id);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        EventServiceGrpc.EventServiceBlockingStub stub = EventServiceGrpc.newBlockingStub(channel);
        var response = stub.logevent(com.example.demo.EventRequest
                .newBuilder()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction("PUT")
                .setEvent("Updated restaurant " + request.getName()).setServiceName("restaurant-service")
                .setUser("Test")
                .build());

        return new ResponseEntity<>(restaurant,HttpStatus.OK);
    }

    @Operation(description = "Get all restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all restaurants in the system",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)) })}
    )
    @GetMapping(path="/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<Restaurant>> getAllRestaurants() {

        var restaurants = restaurantService.getAllRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @Operation(description = "Search for restaurants based on filter and sorting criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all restaurants fulfilling the provided criteria",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page[].class)) })}
    )
    @GetMapping(path="/search")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Page<RestaurantWithRating>> searchForRestaurants(@RequestParam(required = false)  String name,
                                                                                         @RequestParam(required = false)  List<Long> categoryIds,
                                                                                         @RequestParam(required = false)  Boolean isOfferingDiscount,
                                                                                         @RequestParam Integer page,
                                                                                         @RequestParam Integer pageSize,
                                                                                         @RequestParam(required = false) String sortBy,
                                                                                         @RequestParam(required = false) Boolean ascending) {
        FilterRestaurantRequest filterRequest = null;
        if(name!=null || isOfferingDiscount!=null || categoryIds!=null)
            filterRequest = new FilterRestaurantRequest(name,categoryIds,isOfferingDiscount);
        Pageable pageable=PageRequest.of(page,pageSize);
        if(sortBy!=null) {
            if(ascending)
                 pageable = PageRequest.of(page, pageSize, Sort.by(sortBy).ascending());
            else
                 pageable = PageRequest.of(page, pageSize, Sort.by(sortBy).descending());
        }
        var restaurants = restaurantService.searchForRestaurants(filterRequest,pageable);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @Operation(description = "Get a restaurant by restaurant ID")
    @ApiResponses ( value = {
    @ApiResponse(responseCode = "200", description = "Successfully found the restaurant with provided ID",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Restaurant.class)),
            }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
            content = @Content)})
    @GetMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Restaurant> getRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable  Long id) {
        var restaurant = restaurantService.getRestaurant(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @Operation(description = "Get restaurants with categories")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found restaurants with provided categories",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)),
                    })})
    @GetMapping(path="/category")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<Restaurant>> getRestaurantsWithCategories(
            @Parameter(description = "List of category IDs", required = true)
            @RequestParam List<Long> categoryIds)
    {
        return new ResponseEntity<>(restaurantService.getRestaurantsWithCategories(categoryIds),HttpStatus.OK);
    }

    @Operation(description = "Get restaurant average rating")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated average restaurant rating",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)
    })
    @GetMapping(path="/{id}/rating")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Double> getAverageRatingForRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id)
    {
        return new ResponseEntity<>(restaurantService.calculateAverageRatingForRestaurant(id),HttpStatus.OK);
    }

    @Operation(description = "Get user's favorite restaurants")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found user's favorite restaurants",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)),
                    })
    })
    @GetMapping(path="/favorites")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<Restaurant>> getFavoriteRestaurants(
            @Parameter(description = "UUID of the user",required = true)
            @RequestParam String user) {
        return new ResponseEntity<>(favoriteRestaurantService.getFavoriteRestaurants(user),HttpStatus.OK);

    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Delete a restaurant")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the restaurant with provided ID"),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> deleteRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        EventServiceGrpc.EventServiceBlockingStub stub = EventServiceGrpc.newBlockingStub(channel);
        var response = stub.logevent(com.example.demo.EventRequest
                .newBuilder()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction("DELETE")
                .setEvent("Deleted restaurant with id " + id).setServiceName("restaurant-service")
                .setUser("Test")
                .build());

        return new ResponseEntity<>(restaurantService.deleteRestaurant(id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Set restaurant categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated restaurant categories",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path="/{id}/add-categories")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Restaurant> addCategoriesToRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "List of category IDs", required = true)
            @RequestBody List<Long> categoryIds) {
        var restaurant = restaurantService.addCategoriesToRestaurant(id,categoryIds);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        EventServiceGrpc.EventServiceBlockingStub stub = EventServiceGrpc.newBlockingStub(channel);
        var response = stub.logevent(com.example.demo.EventRequest
                .newBuilder()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction("PUT")
                .setEvent("Changed restaurant categories").setServiceName("restaurant-service")
                .setUser("Test")
                .build());


        return  new ResponseEntity<>(restaurant,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Set restaurant opening hours")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated restaurant opening hours",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Restaurant.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path="/{id}/set-opening-hours")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Restaurant> setRestaurantOpeningHours(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Values of daily opening and closing hours", required = true)
            @Valid @RequestBody OpeningHoursCreateRequest request) {
        var restaurant = restaurantService.setRestaurantOpeningHours(id,request);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        EventServiceGrpc.EventServiceBlockingStub stub = EventServiceGrpc.newBlockingStub(channel);
        var response = stub.logevent(com.example.demo.EventRequest
                .newBuilder()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction("PUT")
                .setEvent("Updated restaurant opening hours").setServiceName("restaurant-service")
                .setUser("Test")
                .build());


        return  new ResponseEntity<>(restaurant,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Add restaurant to user's favorite restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added restaurant to favorites",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoriteRestaurant.class)) }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @PostMapping(path="/{id}/add-to-favorites")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<FavoriteRestaurant> addRestaurantToFavorites(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "UUID of the user", required = true)
            @RequestParam String user
    ) {

        var favoriteRestaurant = favoriteRestaurantService.addRestaurantToFavorites(id,user);
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        EventServiceGrpc.EventServiceBlockingStub stub = EventServiceGrpc.newBlockingStub(channel);
        var response = stub.logevent(com.example.demo.EventRequest
                .newBuilder()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction("PUT")
                .setEvent("Added restaurant with id " + id + " to favorites").setServiceName("restaurant-service")
                .setUser("Test")
                .build());

        return new ResponseEntity<>(favoriteRestaurant,HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Remove restaurant from user's favorite restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully removed restaurant from favorites",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoriteRestaurant.class)) }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path="/{id}/remove-from-favorites")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> removeRestaurantFromFavorites(
            @Parameter(description = "Restaurant ID",required = true)
            @PathVariable Long id,
            @Parameter(description = "UUID of the user",required = true)
            @RequestParam String user) {

        favoriteRestaurantService.removeRestaurantFromFavorites(id,user);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        EventServiceGrpc.EventServiceBlockingStub stub = EventServiceGrpc.newBlockingStub(channel);
        var response = stub.logevent(com.example.demo.EventRequest
                .newBuilder()
                .setTimestamp(LocalDateTime.now().toString())
                .setAction("PUT")
                .setEvent("Removed restaurant with id " + id+ " from favorites").setServiceName("restaurant-service")
                .setUser("Test")
                .build());

        return new ResponseEntity<>("Successfully removed restaurant with id " + id + " from favorites!",HttpStatus.OK);
    }

    @Operation(description = "Get restaurant UUID by restaurant ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found restaurant UUID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "Restaurant with provided ID not found",
                    content = @Content)}
    )
    @GetMapping(path="/uuid/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> getRestaurantUUIDByRestaurantId(
            @Parameter(description = "Restaurant ID",required = true)
            @PathVariable Long id) {
        return new ResponseEntity<>(restaurantService.getRestaurantUUID(id),HttpStatus.OK);
    }
}

