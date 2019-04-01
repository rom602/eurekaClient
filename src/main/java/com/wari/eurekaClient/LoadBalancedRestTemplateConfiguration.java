package com.wari.eurekaClient;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LoadBalancedRestTemplateConfiguration {
    // @LoadBalanced 는 일종의 예비 심사(qualifier) 애노테이션이다.
    // 스프링은 예비 심사 애노테이션을 통해 빈의 정의를 좀더 분명히 하고 특별한 처리가 수행되게 한다.
    // 예제에서는 RestTemplate 인스턴스에 @LoadBalanced 애노테이션을 붙여서 로드밸런싱 인터셉터를 설정한다.
    @Bean
    @LoadBalanced
    WebClient webClient() {
        return WebClient.builder().build();
    }
}
