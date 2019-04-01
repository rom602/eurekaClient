package com.wari.eurekaClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;

@Component
public class LoadBalancedRestTemplateCLR implements CommandLineRunner {
    private final Log log = LogFactory.getLog(getClass());

    private final WebClient client;

    LoadBalancedRestTemplateCLR(@LoadBalanced WebClient client) {
        this.client = client;
    }

    // @LoadBalanced 애노테이션이 붙은 RestTemplate 을 주입해서 LoadBalancedRestTemplateCLR 을 생성한다.
    @Override
    public void run(String... args) throws Exception {

        Map<String, String> variables = Collections.singletonMap("name",
                "Cloud Natives!");

        // RestTemplate 으로 API 를 호출할 때 IP 주소나 DNS 이름 대신 greetings-service 라는 서비스 ID 를 사용한다.
        this.client.get().uri("http://greetings-service/hi/{name}", variables)
                .retrieve().bodyToMono(JsonNode.class).map(x -> x.get("greeting").asText())
                .subscribe(greeting -> log.info("greeting: " + greeting));
    }
}
