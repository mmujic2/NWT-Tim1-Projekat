package the.convenient.foodie.restaurant.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.restaurant.dao.RestaurantRepository;
import the.convenient.foodie.restaurant.entity.Restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Arrays.stream;

@RestController
@RequestMapping(path="/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Restaurant> addNewRestaurant (@RequestParam String name
            , @RequestParam String managerUUID,@RequestParam String userId) {

        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setManagerUUID(managerUUID);
        restaurant.setCreated(LocalDateTime.now());
        restaurant.setCreatedBy(userId);
        restaurantRepository.save(restaurant);
        return new ResponseEntity<>(restaurant,HttpStatus.CREATED);
    }

    @GetMapping(path="/all")
    public @ResponseBody ResponseEntity<List<Restaurant>> getAllRestaurants() {

        return new ResponseEntity<>(StreamSupport.stream(restaurantRepository.findAll().spliterator(),false).collect(Collectors.toList()), HttpStatus.OK);
    }
}

