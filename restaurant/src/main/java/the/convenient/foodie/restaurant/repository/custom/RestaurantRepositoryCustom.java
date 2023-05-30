package the.convenient.foodie.restaurant.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import the.convenient.foodie.restaurant.dto.restaurant.FilterRestaurantRequest;
import the.convenient.foodie.restaurant.dto.restaurant.RestaurantResponse;
import the.convenient.foodie.restaurant.dto.restaurant.RestaurantShortResponse;

import java.util.List;

public interface RestaurantRepositoryCustom {

    List<RestaurantShortResponse> getRestaurants(FilterRestaurantRequest filters, String sortBy, Boolean ascending);

    String getRestaurantUUID(Long id);

    RestaurantShortResponse getRestaurantShortResponseById(Long id);

    RestaurantResponse getRestaurantFullResponseById(Long id);
}
