package fooide.config.server.foodieConfigServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient(autoRegister = true)
public class FoodieConfigServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(FoodieConfigServerApplication.class, args);
	}

	@RestController
	@RequestMapping(path="/health")
	public class HealthCheck {
		@GetMapping("/check")
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity<String> healthCheck() {
			return new ResponseEntity<>("ok", HttpStatus.OK);
		}
	}

}
