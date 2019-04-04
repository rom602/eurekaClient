package com.wari.eurekaClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Component
public class LoadBalancedRestTemplateCLR implements CommandLineRunner {

    private final RestTemplate restTemplate;

    private final Log log = LogFactory.getLog(getClass());

    // @LoadBalanced 애노테이션이 붙은 RestTemplate 을 주입해서 LoadBalancedRestTemplateCLR 을 생성한다.
    @Autowired
    public LoadBalancedRestTemplateCLR(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, String> variables =
                Collections.singletonMap("name", "Cloud Natives!");

        // RestTemplate 으로 API 를 호출할 때 IP 주소나 DNS 이름 대신 greetings-service 라는 서비스 ID 를 사용한다.
        ResponseEntity<JsonNode> response =
                this.restTemplate
                        .getForEntity("//greetings-service/hi/{name}", JsonNode.class, variables);

        JsonNode body = response.getBody();
        String greeting = body.get("greeting").asText();
        log.info("greeting: " + greeting);
    }
}
