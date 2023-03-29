package the.convenient.foodie.restaurant.controller;

import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.restaurant.dto.OpeningHoursCreateRequest;
import the.convenient.foodie.restaurant.dto.RestaurantCreateRequest;
import the.convenient.foodie.restaurant.dto.RestaurantUpdateRequest;
import the.convenient.foodie.restaurant.model.FavoriteRestaurant;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.service.FavoriteRestaurantService;
import the.convenient.foodie.restaurant.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping(path="/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private FavoriteRestaurantService favoriteRestaurantService;


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
        return new ResponseEntity<>(restaurant,HttpStatus.CREATED);
    }

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
        return new ResponseEntity<>(restaurantService.deleteRestaurant(id),HttpStatus.OK);
    }

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
        return  new ResponseEntity<>(restaurant,HttpStatus.OK);
    }

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
        return  new ResponseEntity<>(restaurant,HttpStatus.OK);
    }

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
            @PathVariable Long id
    ) {
        //Update this with ID of User
        var userUUID = "test";
        var favoriteRestaurant = favoriteRestaurantService.addRestaurantToFavorites(id,userUUID);
        return new ResponseEntity<>(favoriteRestaurant,HttpStatus.CREATED);
    }

}

