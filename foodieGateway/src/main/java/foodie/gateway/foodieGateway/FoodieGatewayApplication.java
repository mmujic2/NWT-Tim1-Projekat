package foodie.gateway.foodieGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EnableDiscoveryClient(autoRegister = true)
public class FoodieGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodieGatewayApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RouteLocator test(RouteLocatorBuilder builder) {
		// Moze se i svaka ruta posebno staviti, ovo ce mozda
		// biti potrebno ako budemo imali preklapajuce nazive
		/*return builder.routes()
				.route("order-service-get-one", p -> p
						.path("/order/get/1")
						.uri("lb://order-service"))
				.route("order-service-get-all", p -> p
						.path("/order/get")
						.uri("lb://order-service"))
				.build();*/

		// /order/** znaci svaka ruta koja pocinje sa localhost:8000/order
		// ce biti preusmjerena na order servis npr.
		// localhost:8000/order/get ce biti order-service/order/get
		return builder.routes()
				.route("order-service-get-one", p -> p
						.path("/order/**")
						.uri("lb://order-service"))
				.route("discountservice", p -> p
						.path("/coupon/**")
						.uri("lb://discount-service"))
				.build();

	}
}
