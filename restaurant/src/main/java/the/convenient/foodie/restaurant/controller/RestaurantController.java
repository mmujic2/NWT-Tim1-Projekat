package the.convenient.foodie.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import the.convenient.foodie.restaurant.dao.RestaurantRepository;
import the.convenient.foodie.restaurant.entity.Restaurant;

import java.time.LocalDateTime;

@Controller // This means that this class is a Controller
@RequestMapping(path="/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String addNewRestaurant (@RequestParam String name
            , @RequestParam String managerUUID,@RequestParam String userId) {

        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setManagerUUID(managerUUID);
        restaurant.setCreated(LocalDateTime.now());
        restaurant.setCreatedBy(userId);
        restaurantRepository.save(restaurant);
        return "Saved";
    }

    @GetMapping(path="/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<Restaurant> getAllRestaurants() {

        return restaurantRepository.findAll();
    }
}

