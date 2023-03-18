package the.convenient.foodie.restaurant.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.entity.FavoriteRestaurant;

@Repository
public interface FavoriteRestaurantRepository extends CrudRepository<FavoriteRestaurant,Long> {
}
