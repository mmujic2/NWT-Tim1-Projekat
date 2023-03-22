package the.convenient.foodie.menu.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.menu.entity.MenuItem;

@Repository
public interface MenuItemRepository extends CrudRepository<MenuItem, Long> {
}
