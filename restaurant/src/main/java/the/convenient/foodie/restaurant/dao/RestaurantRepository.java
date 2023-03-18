package the.convenient.foodie.restaurant.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.entity.Restaurant;

@Repository
public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
}
