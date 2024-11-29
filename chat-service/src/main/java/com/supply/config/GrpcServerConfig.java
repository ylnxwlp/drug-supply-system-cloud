package com.supply.config;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.supply.mapper.ChatMapper;
import com.supply.service.impl.ChatServiceImpl;
import com.supply.websocket.ChatEndPoint;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.net.InetAddress;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GrpcServerConfig {

    private Server server;

    private final ChatMapper chatMapper;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final ChatEndPoint chatEndPoint;

    private final DiscoveryClient discoveryClient;

    @PostConstruct
    public void start() throws IOException, NacosException {
        // 绑定端口
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(0);
        serverBuilder.addService(new ChatServiceImpl(chatMapper,chatEndPoint,redisTemplate,discoveryClient));
        server = serverBuilder.build().start();
        int port = server.getPort(); // 获取随机分配的端口
        NamingService namingService = NamingFactory.createNamingService("192.168.65.151:8848");
        // 获取本机 IP 地址
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        namingService.registerInstance("chat-service-grpc", hostAddress, port); // 注册实例
        log.info("chatService的gRPC服务启动，监听端口：{}", port);
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
            log.info("chatService的gRPC服务停止……");

        }
    }
}
