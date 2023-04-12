/*package the.convenient.foodie.order.config;

import com.example.demo.EventServiceGrpc;
import io.swagger.v3.oas.models.OpenAPI;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.web.client.RestTemplate;

@Configuration
@GrpcClientBean(
        clazz = EventServiceGrpc.EventServiceBlockingStub.class,
        beanName = "blockingStub",
        client = @GrpcClient("test")
)
public class rpcConfig {

    @Bean
    EventLogger EventLoggerService(@Autowired EventServiceGrpc.EventServiceBlockingStub blockingStub) {
        return new EventLogger(blockingStub);
    }
}*/