package the.convenient.foodie.restaurant.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {

        Info info = new Info()
                .title("The Convenient Foodie - Restaurant API")
                .version("1.0")
                .description("This API exposes endpoints of the Restaurant microservice.");

        return new OpenAPI().info(info);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}