package the.convenient.foodie.restaurant.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("order-service")
public interface OrderFeignClient {
    @PostMapping("/order/count/{sort}") public List<String> getNumberOfOrdersPerRestaurant(@RequestBody List<String> restaurants, @PathVariable String sort);

}
