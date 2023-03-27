package the.convenient.foodie.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import the.convenient.foodie.order.model.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    // @Override
    // Iterable<MenuItem> findAllById(Iterable<Long> longs);
}
