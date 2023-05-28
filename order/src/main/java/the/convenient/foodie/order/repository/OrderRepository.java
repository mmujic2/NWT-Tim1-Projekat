package the.convenient.foodie.order.repository;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import the.convenient.foodie.order.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT t FROM Order t WHERE t.userId=:uuid ORDER BY t.createdTime DESC")
    public List<Order> getOrdersByUserUUID(String uuid);

    @Query("SELECT t FROM Order t WHERE t.orderStatus='Ready for delivery' ORDER BY t.createdTime")
    public List<Order> getReadyForDeliveryOrders();

    @Query("SELECT t FROM Order t WHERE t.deliveryPersonId=:uuid AND t.orderStatus='In delivery' ORDER BY t.createdTime")
    public List<Order> getOrdersByDeliveryPersonId(String uuid);
}
