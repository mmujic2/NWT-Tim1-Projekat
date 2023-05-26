package foodie.gateway.foodieGateway;

import foodie.gateway.foodieGateway.filter.AuthenticationPrefilter;
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
	public RouteLocator routes(RouteLocatorBuilder builder, AuthenticationPrefilter authFilter) {
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
				.route("order-service-order", p -> p
						.path("/order/**")
						.filters(f ->
								f.filter(authFilter.apply(
								new AuthenticationPrefilter.Config())))
						.uri("lb://order-service"))
				.route("order-service-menu-item", p -> p
						.path("/menuitem/**")
						.filters(f ->
								f.filter(authFilter.apply(
								new AuthenticationPrefilter.Config())))
						.uri("lb://order-service"))
				.route("discount-service-coupon", p -> p
						.path("/coupon/**")
						.filters(f ->
								f.filter(authFilter.apply(
												new AuthenticationPrefilter.Config())))
						.uri("lb://discount-service"))
				.route("discount-service-required-score", p -> p
						.path("/requiredscore/**")
						.filters(f ->
								f.filter(authFilter.apply(
								new AuthenticationPrefilter.Config())))
						.uri("lb://discount-service"))
				.route("discount-service-score", p -> p
						.path("/score/**")
						.filters(f ->
								f.filter(authFilter.apply(
								new AuthenticationPrefilter.Config())))
						.uri("lb://discount-service"))
				.route("restaurant-service-restaurant", p -> p
						.path("/restaurant/**")
						.filters(f ->
								f.filter(authFilter.apply(
								new AuthenticationPrefilter.Config())))
						.uri("lb://restaurant-service"))
				.route("restaurant-service-category", p -> p
						.path("/category/**")
						.filters(f ->
								f.rewritePath("/restaurant-service(?<segment>/?.*)", "$\\{segment}")
								.filter(authFilter.apply(
								new AuthenticationPrefilter.Config())))
						.uri("lb://restaurant-service"))
				.route("restaurant-service-review", p -> p
						.path("/review/**")
						.filters(f ->
								f.filter(authFilter.apply(
								new AuthenticationPrefilter.Config())))
						.uri("lb://restaurant-service"))
				.route("menu-service-menu-item", p -> p
						.path("/menu-item/**")
						.filters(f ->
								f.filter(authFilter.apply(
								new AuthenticationPrefilter.Config())))
						.uri("lb://menu-service"))
				.route("menu-service-menu", p -> p
						.path("/menu/**")
						.filters(f ->
								f.filter(authFilter.apply(
								new AuthenticationPrefilter.Config())))
						.uri("lb://menu-service"))
				.route("auth-service", p -> p
						.path("/user/**")
						.filters(f ->
								f.filter(authFilter.apply(
										new AuthenticationPrefilter.Config())))
						.uri("lb://auth-service"))
				.route("auth-service", p -> p
						.path("/auth/**")
						.uri("lb://auth-service"))
				.build();

	}
}
