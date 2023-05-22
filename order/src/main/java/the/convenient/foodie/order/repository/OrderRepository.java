package the.convenient.foodie.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import the.convenient.foodie.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
