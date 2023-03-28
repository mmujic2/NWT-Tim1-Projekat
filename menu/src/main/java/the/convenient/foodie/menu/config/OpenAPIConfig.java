package the.convenient.foodie.menu.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {

        Info info = new Info()
                .title("The Convenient Foodie - Menu API")
                .version("1.0")
                .description("This API exposes endpoints of the Menu microservice.");

        return new OpenAPI().info(info);
    }
}