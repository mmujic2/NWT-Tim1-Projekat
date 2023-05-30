package the.convenient.foodie.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.model.RestaurantImage;

import java.util.List;

@Repository
public interface RestaurantImageRepository extends JpaRepository<RestaurantImage,Long> {
    @Query("SELECT ri FROM RestaurantImage ri WHERE ri.restaurant.id=:restaurantId")
    public List<RestaurantImage> findAllByRestaurantId(Long restaurantId);
}
