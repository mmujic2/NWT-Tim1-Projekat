package the.convenient.foodie.restaurant.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient("order-service")
public interface OrderFeignClient {
    @PostMapping("/order/count/{sort}") public Map<String,Long> getNumberOfOrdersPerRestaurant(@RequestBody List<String> restaurants, @PathVariable String sort);

}
