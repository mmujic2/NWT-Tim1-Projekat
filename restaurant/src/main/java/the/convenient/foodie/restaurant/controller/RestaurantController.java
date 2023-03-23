package the.convenient.foodie.restaurant.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.restaurant.dao.dto.OpeningHoursCreateRequest;
import the.convenient.foodie.restaurant.dao.dto.RestaurantCreateRequest;
import the.convenient.foodie.restaurant.dao.dto.RestaurantUpdateRequest;
import the.convenient.foodie.restaurant.entity.Restaurant;
import the.convenient.foodie.restaurant.service.RestaurantService;

import java.util.List;

import static java.util.Arrays.stream;

@RestController
@RequestMapping(path="/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;


    @Operation(description = "Create a new restaurant with basic information")
    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Restaurant> addNewRestaurant (@Valid @RequestBody RestaurantCreateRequest request) {

        var restaurant = restaurantService.addNewRestaurant(request);
        return new ResponseEntity<>(restaurant,HttpStatus.CREATED);
    }

    @Operation(description = "Update restaurant information")
    @PutMapping(path="/update/{id}")
    public @ResponseBody ResponseEntity<Restaurant> addNewRestaurant (@PathVariable Long id, @RequestBody
    @Valid RestaurantUpdateRequest request) {

        Restaurant restaurant = null;
        restaurant = restaurantService.updateRestaurant(request,id);

        return new ResponseEntity<>(restaurant,HttpStatus.CREATED);
    }

    @Operation(description = "Get all restaurants")
    @GetMapping(path="/all")
    public @ResponseBody ResponseEntity<List<Restaurant>> getAllRestaurants() {

        var restaurants = restaurantService.getAllRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @Operation(description = "Get a restaurant by restaurant ID")
    @GetMapping(path="/{id}")
    public @ResponseBody ResponseEntity<Restaurant> getRestaurant(@PathVariable  Long id) {
        var restaurant = restaurantService.getRestaurant(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @Operation(description = "Delete a restaurant")
    @DeleteMapping(path="/{id}")
    public @ResponseBody ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        return new ResponseEntity<>(restaurantService.deleteRestaurant(id),HttpStatus.OK);
    }

    @Operation(description = "Set restaurant categories")
    @PutMapping(path="/{id}/add-categories")
    public @ResponseBody ResponseEntity<Restaurant> addCategoriesToRestaurant(@PathVariable Long id,@RequestBody List<Long> categoryIds) {
        var restaurant = restaurantService.addCategoriesToRestaurant(id,categoryIds);
        return  new ResponseEntity<>(restaurant,HttpStatus.CREATED);
    }

    @Operation(description = "Set restaurant opening hours")
    @PutMapping(path="/{id}/set-opening-hours")
    public @ResponseBody ResponseEntity<Restaurant> setRestaurantOpeningHours(@PathVariable Long id,@Valid @RequestBody OpeningHoursCreateRequest request) {
        var restaurant = restaurantService.setRestaurantOpeningHours(id,request);
        return  new ResponseEntity<>(restaurant,HttpStatus.CREATED);
    }

}

