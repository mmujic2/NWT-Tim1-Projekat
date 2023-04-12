package the.convenient.foodie.restaurant.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import the.convenient.foodie.restaurant.dto.FilterRestaurantRequest;
import the.convenient.foodie.restaurant.dto.RestaurantWithRating;
import the.convenient.foodie.restaurant.model.Restaurant;

public interface RestaurantRepositoryCustom {

    Page<RestaurantWithRating> getRestaurants(FilterRestaurantRequest filters, Pageable pageable);

    String getRestaurantUUID(Long id);
}
