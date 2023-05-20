package com.example.demo;

import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@SpringBootApplication
@EnableEurekaServer
public class FoodieserverApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		Server server = ServerBuilder.forPort(9090).addService(new EventService()).build();
		server.start();
		// server.awaitTermination();

		SpringApplication.run(FoodieserverApplication.class, args);
	}


	@RestController
	class ServiceInstanceRestController {

		@Autowired
		private FoodieEventRepository foodieEventRepository;

		@Autowired
		private FoodieserverApplication discoveryClient;

		@GetMapping("/api/discovery/instance/info")
		public ResponseEntity<?> test() {
			show();
			return ResponseEntity.ok("no");
		}

		public void show() {
			PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
			Applications applications = registry.getApplications();

			applications.getRegisteredApplications().forEach((registeredApplication) -> {
				registeredApplication.getInstances().forEach((instance) -> {
					System.out.println(instance.getAppName() + " (" + instance.getInstanceId() + ") : ");
				});
			});
		}

		@PostConstruct
		public void init() {
			RepositoryContainer.foodieEventRepository = this.foodieEventRepository;
		}

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
