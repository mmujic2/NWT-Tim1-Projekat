package the.convenient.foodie.order;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderMenuItems, Integer> {

}
