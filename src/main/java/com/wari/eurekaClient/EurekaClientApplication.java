package com.wari.eurekaClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

// 스프링 클라우드 DiscoveryClient 추상화를 활성화한다.
//@EnableDiscoveryClient
@SpringBootApplication
public class EurekaClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaClientApplication.class, args);
	}

	// greeting 키를 포함하는 JSON을 반환한다.
	@RestController
	class GreetingRestController {
		@GetMapping("/hi/{name}")
		Map<String, String> hi(@PathVariable String name,
							   @RequestHeader(value = "X-CNJ-Name", required = false)
									   Optional<String> cn) {
			String resovledName = cn.orElse(name);
			return Collections.singletonMap("greeting", "Hello, " +
					resovledName + "!");
		}
	}
}

