package com.wari.eurekaClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

@Component
public class DiscoveryClientCLR implements CommandLineRunner {

    private final DiscoveryClient discoveryClient;

    private Log log = LogFactory.getLog(getClass());

    // 스프링 클라우드의 DiscoveryClient 구현체를 주입해서 DiscoveryClientCLR 을 생성한다.
    @Autowired
    public DiscoveryClientCLR(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public void run(String... args) throws Exception {
        // 등록된 모든 greetings-service 인스턴스를 찾고 각 인스턴스의 호스트 이름,
        // 포트 번호, 서비스 ID를 출력한다.
        String serviceId = "greetings-service";
        this.log.info(String.format("registered instances of '%s'", serviceId));
        this.discoveryClient.getInstances(serviceId)
                .forEach(this::logServiceInstance);
    }

    private void logServiceInstance(ServiceInstance si) {
        String msg = String.format("host = %sm port = %s, service ID = %s",
                si.getHost(), si.getPort(), si.getServiceId());
        log.info(msg);
    }
}
