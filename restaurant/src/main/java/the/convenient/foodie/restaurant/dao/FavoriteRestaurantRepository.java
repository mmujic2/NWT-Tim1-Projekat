package the.convenient.foodie.restaurant.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.entity.FavoriteRestaurant;

@Repository
public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant,Long> {

}
