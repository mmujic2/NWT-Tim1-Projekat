package the.convenient.foodie.menu.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.menu.entity.Menu;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Long> {
}
