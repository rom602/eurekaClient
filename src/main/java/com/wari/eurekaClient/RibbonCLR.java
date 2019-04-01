package com.wari.eurekaClient;

import com.netflix.loadbalancer.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class RibbonCLR implements CommandLineRunner {

    private final DiscoveryClient discoveryClient;

    private final Log log = LogFactory.getLog(getClass());

    public RibbonCLR(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public void run(String... args) throws Exception {

        String serviceId = "greetings-service";

        // 스프링 클라우드의 DiscoveryClient 구현체를 주입하고 RibbonCLR 객체를 생성한다.
        List<Server> servers = this.discoveryClient.getInstances(serviceId).stream()
                .map(si -> new Server(si.getHost(), si.getPort()))
                .collect(Collectors.toList());

        // 현재 인스턴스의 정보를 얻는다.
        // 현재 실행 중인 애플리케이션 정보 중에서 어떤 것이 유레카 서비스에 등록될까?
        IRule roundRobinRule = new RoundRobinRule();

        BaseLoadBalancer loadBalancer = LoadBalancerBuilder.newBuilder()
                .withRule(roundRobinRule).buildFixedServerListLoadBalancer(servers);

        IntStream.range(0, 10).forEach(i -> {
            // 등록된 모든 greetings-service 인스턴스를 찾고 각 인스턴스의 호스트 이름, 포트 번호,
            // 서비스 ID 를 출력한다.
            Server server = loadBalancer.chooseServer();
            URI uri = URI.create("http://" + server.getHost() + ":" + server.getPort()
                    + "/");
            log.info("resolved service " + uri.toString());
        });
    }
}
