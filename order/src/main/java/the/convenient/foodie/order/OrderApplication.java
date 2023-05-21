package the.convenient.foodie.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableDiscoveryClient(autoRegister = true)
public class OrderApplication {



	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
