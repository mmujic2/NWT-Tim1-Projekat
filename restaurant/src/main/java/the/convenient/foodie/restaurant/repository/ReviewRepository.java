package the.convenient.foodie.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

}
