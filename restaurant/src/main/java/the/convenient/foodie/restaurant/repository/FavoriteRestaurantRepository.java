package the.convenient.foodie.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.model.FavoriteRestaurant;

@Repository
public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant,Long> {

}
