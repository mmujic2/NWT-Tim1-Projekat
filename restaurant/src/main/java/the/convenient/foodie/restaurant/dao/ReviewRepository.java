package the.convenient.foodie.restaurant.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

}
