package fooide.config.server.foodieConfigServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class FoodieConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodieConfigServerApplication.class, args);
	}

}
