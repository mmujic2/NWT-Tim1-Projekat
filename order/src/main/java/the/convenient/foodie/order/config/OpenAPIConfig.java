package the.convenient.foodie.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {

        Info info = new Info()
                .title("The Convenient Foodie - Order API")
                .version("0.1")
                .description("This API exposes endpoints of the Order microservice.");

        return new OpenAPI().info(info);
    }
}