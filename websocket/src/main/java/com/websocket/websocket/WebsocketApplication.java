package com.websocket.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@EnableDiscoveryClient(autoRegister = true)
public class WebsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketApplication.class, args);
	}

	@RestController
	@RequestMapping(path="/websocket")
	public class test {
		@Autowired
		SimpMessagingTemplate simpMessagingTemplate;

		@PostMapping(path = "/message/{token}")
		public String sendSocketMessage(@PathVariable String token, @RequestBody String message) {
			simpMessagingTemplate.convertAndSend("/message/" + token, message);
			return message;
		}
	}

}
