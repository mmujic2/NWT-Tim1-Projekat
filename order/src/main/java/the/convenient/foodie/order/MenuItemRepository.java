package the.convenient.foodie.order;

import org.springframework.data.repository.CrudRepository;

public interface MenuItemRepository extends CrudRepository<MenuItem, Long> {
    @Override
    Iterable<MenuItem> findAllById(Iterable<Long> longs);
}
