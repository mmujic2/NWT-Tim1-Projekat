package the.convenient.foodie.discount.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {

        Info info = new Info()
                .title("The Convenient Foodie - Discount API")
                .version("1.0")
                .description("This API contains endpoints of the Discount microservice.");

        return new OpenAPI().info(info);
    }

}
