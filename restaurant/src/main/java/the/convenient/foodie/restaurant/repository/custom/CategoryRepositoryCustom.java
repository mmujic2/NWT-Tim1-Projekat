package the.convenient.foodie.restaurant.repository.custom;

import the.convenient.foodie.restaurant.model.Restaurant;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Restaurant> getRestaurantsWithCategory(Long categoryId);
}
