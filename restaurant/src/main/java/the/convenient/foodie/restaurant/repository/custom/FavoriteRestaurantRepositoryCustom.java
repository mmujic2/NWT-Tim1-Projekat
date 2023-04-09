package the.convenient.foodie.restaurant.repository.custom;

import the.convenient.foodie.restaurant.model.Restaurant;

import java.util.List;

public interface FavoriteRestaurantRepositoryCustom {

    List<Restaurant> getFavoriteRestaurants(String userUUID);
    public void removeRestaurantFromFavorites(Long restaurantId, String userUUID);
}
