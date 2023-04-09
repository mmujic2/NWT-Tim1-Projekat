package the.convenient.foodie.restaurant.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.restaurant.model.Restaurant;
import the.convenient.foodie.restaurant.repository.FavoriteRestaurantRepository;
import the.convenient.foodie.restaurant.repository.RestaurantRepository;
import the.convenient.foodie.restaurant.model.FavoriteRestaurant;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteRestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private FavoriteRestaurantRepository favoriteRestaurantRepository;

    public List<Restaurant> getFavoriteRestaurants(String userUUID) {

        return favoriteRestaurantRepository.getFavoriteRestaurants(userUUID);
    }
    public FavoriteRestaurant addRestaurantToFavorites(Long restaurantId,String userUUID) {
        FavoriteRestaurant favoriteRestaurant = new FavoriteRestaurant();
        var exception = new EntityNotFoundException("Restaurant with id " + restaurantId + " does not exist!");
        var restaurant = restaurantRepository.findById(restaurantId).orElseThrow(()-> exception);
        favoriteRestaurant.setRestaurant(restaurant);
        favoriteRestaurant.setUserUUID(userUUID);
        favoriteRestaurant.setCreated(LocalDateTime.now());
        //Update with userID/name
        favoriteRestaurant.setCreatedBy(userUUID);
        favoriteRestaurantRepository.save(favoriteRestaurant);

        return favoriteRestaurant;
    }

    public void removeRestaurantFromFavorites(Long restaurantId, String userUUID)  {
        var restaurant = restaurantRepository.findById(restaurantId).orElseThrow(()->new EntityNotFoundException("Restaurant with id " + restaurantId + " does not exist!"));
        favoriteRestaurantRepository.removeRestaurantFromFavorites(restaurantId, userUUID);
    }



}
