package the.convenient.foodie.order.repository;

import org.springframework.data.repository.CrudRepository;
import the.convenient.foodie.order.model.MenuItem;

public interface MenuItemRepository extends CrudRepository<MenuItem, Long> {
    @Override
    Iterable<MenuItem> findAllById(Iterable<Long> longs);
}
