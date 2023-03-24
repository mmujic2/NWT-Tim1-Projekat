package the.convenient.foodie.order.repository;

import org.springframework.data.repository.CrudRepository;
import the.convenient.foodie.order.model.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
