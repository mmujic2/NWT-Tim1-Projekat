package the.convenient.foodie.menu.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.amqp.core.Queue;

@Configuration
public class OpenAPIConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenAPI myOpenAPI() {

        Info info = new Info()
                .title("The Convenient Foodie - Menu API")
                .version("1.0")
                .description("This API exposes endpoints of the Menu microservice.");

        return new OpenAPI().info(info);
    }

    @Bean
    public Queue menuItemCreateQueue() {
        return new Queue("menuItemCreate", false);
    }

    @Bean
    public Queue menuItemCreateErrorQueue() {
        return new Queue("menuItemCreateError", false);
    }
}