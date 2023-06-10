package the.convenient.foodie.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.model.Review;
import the.convenient.foodie.restaurant.repository.custom.ReviewRepositoryCustom;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>, ReviewRepositoryCustom {

    @Query("SELECT r FROM Review r WHERE r.userUUID=:userUUID AND r.restaurant.id=:restaurantId")
    public Review findByUserAndRestaurant(String userUUID, Long restaurantId);
}
