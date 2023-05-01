package the.convenient.foodie.restaurant.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("discount-service")
public interface DiscountFeignClient {

    @PostMapping("/coupon/filter") public List<String> filterDiscountedRestaurants(@RequestBody List<String> restaurants);
}
