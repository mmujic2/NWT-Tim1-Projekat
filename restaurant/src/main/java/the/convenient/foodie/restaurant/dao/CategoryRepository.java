package the.convenient.foodie.restaurant.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {


}
