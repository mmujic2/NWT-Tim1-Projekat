package the.convenient.foodie.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.restaurant.model.Category;
import the.convenient.foodie.restaurant.repository.custom.CategoryRepositoryCustom;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>, CategoryRepositoryCustom {


}
