package com.example.demo;

import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableEurekaServer
public class FoodieserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodieserverApplication.class, args);
	}


	@RestController
	class ServiceInstanceRestController {

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




	}



}
