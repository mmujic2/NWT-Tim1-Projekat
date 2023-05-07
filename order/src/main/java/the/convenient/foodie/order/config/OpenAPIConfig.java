package the.convenient.foodie.order.config;

import com.example.demo.EventServiceGrpc;
import com.rabbitmq.client.AMQP;
import io.swagger.v3.oas.models.OpenAPI;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import org.springframework.amqp.core.Queue;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.web.client.RestTemplate;

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
                .title("The Convenient Foodie - Order API")
                .version("0.1")
                .description("This API exposes endpoints of the Order microservice.");

        return new OpenAPI().info(info);
    }

    @Bean
    public Queue testQueue() {
        return new Queue("testQueue", false);
    }
}