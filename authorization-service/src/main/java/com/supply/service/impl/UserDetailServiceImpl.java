package com.supply.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.supply.api.user.UserProto;
import com.supply.api.user.UserServiceGrpc;
import com.supply.constant.MessageConstant;
import com.supply.domain.entity.LoginUser;
import com.supply.domain.entity.User;
import com.supply.exception.InstanceNotFoundException;
import com.supply.exception.LoginErrorException;
import com.supply.service.DrugUserDetailService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements DrugUserDetailService {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final DiscoveryClient discoveryClient;

    private final AtomicInteger index = new AtomicInteger(0);

    public UserDetails loadUserByUsernameAndFirmName(String username, String firmName) throws UsernameNotFoundException {
        if (!Objects.isNull(redisTemplate.opsForValue().get("user doesn't exist:" + username + firmName))) {
            throw new LoginErrorException(MessageConstant.INFORMATION_ERROR);
        }
        // 进行远程调用获取用户信息
        // 开始创建通信管道
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service-grpc");
        if (instances == null || instances.isEmpty()) {
            throw new InstanceNotFoundException(MessageConstant.SERVER_ERROR);
        }
        // 实现轮询
        if (index.get() > Integer.MAX_VALUE - 1000) {
            index.set(0);
        }
        int currentIndex = index.getAndIncrement() % instances.size();
        URI uri = instances.get(currentIndex).getUri();
        String host = uri.getHost();
        int port = uri.getPort();
        log.info("当前轮询到：{}：{}", host,port);
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        UserProto.authenticationResponse authenticationResponse;
        try {
            UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel);
            UserProto.authenticationRequest.Builder builder = UserProto.authenticationRequest.newBuilder();
            if (!username.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
                builder
                        .setUsername(username)
                        .setFirmName(firmName)
                        .build();
            } else {
                builder
                        .setEmail(username)
                        .setFirmName(firmName)
                        .build();
            }
            UserProto.authenticationRequest re = builder.build();
            //完成RPC调用
            authenticationResponse = userServiceBlockingStub.getUserInformationById(re);
            log.info("调用用户信息返回结果：{}", authenticationResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        User u = new User();
        BeanUtils.copyProperties(authenticationResponse, u);
        if (u.getId() == null) {
            //防止缓存穿透，缓存空数据并设置过期时间
            redisTemplate.opsForValue().set("user doesn't exist:" + username + firmName, "1", 25 + RandomUtil.randomInt(10), TimeUnit.MINUTES);
            throw new LoginErrorException(MessageConstant.INFORMATION_ERROR);
        }
        //查询对应的权限信息
        try {
            UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub2 = UserServiceGrpc.newBlockingStub(managedChannel);
            UserProto.authorityRequest.Builder builder2 = UserProto.authorityRequest.newBuilder();
            UserProto.authorityRequest res = builder2.setId(u.getId()).build();
            //完成RPC调用
            UserProto.authorityResponse authorityResponse = userServiceBlockingStub2.getUserAuthority(res);
            log.info("调用用户权限返回结果：{}", authorityResponse.getAuthoritiesList());
            List<String> authorities = authorityResponse.getAuthoritiesList();
            //把数据封装为UserDetails对象返回
            return new LoginUser(u, authorities);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            managedChannel.shutdown();
        }
    }
}