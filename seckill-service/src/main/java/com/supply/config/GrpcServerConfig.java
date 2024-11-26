package com.supply.config;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.supply.mapper.SeckillMapper;
import com.supply.service.impl.SeckillServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.net.InetAddress;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GrpcServerConfig {

    private Server server;

    private final SeckillMapper seckillMapper;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final RedissonClient redissonClient;

    private final RabbitTemplate rabbitTemplate;

    private final DiscoveryClient discoveryClient;

    @PostConstruct
    public void start() throws IOException, NacosException {
        // 绑定端口
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(0);
        serverBuilder.addService(new SeckillServiceImpl(seckillMapper,redisTemplate,redissonClient,rabbitTemplate,discoveryClient));
        server = serverBuilder.build().start();
        int port = server.getPort(); // 获取随机分配的端口
        NamingService namingService = NamingFactory.createNamingService("192.168.65.151:8848");
        // 获取本机 IP 地址
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        namingService.registerInstance("seckill-service-grpc", hostAddress, port); // 注册实例
        log.info("userService的gRPC服务启动，监听端口：{}", port);
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
            log.info("userService的gRPC服务停止……");

        }
    }
}
