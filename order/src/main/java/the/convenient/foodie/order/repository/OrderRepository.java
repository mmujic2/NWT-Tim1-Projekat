package the.convenient.foodie.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import the.convenient.foodie.order.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT t FROM Order t WHERE t.userId=:uuid ORDER BY t.createdTime DESC")
    public List<Order> getOrdersByUserUUID(String uuid);
}
