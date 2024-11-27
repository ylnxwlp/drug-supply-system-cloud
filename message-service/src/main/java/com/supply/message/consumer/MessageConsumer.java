package com.supply.message.consumer;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supply.api.chat.ChatProto;
import com.supply.api.chat.ChatServiceGrpc;
import com.supply.constant.MessageConstant;
import com.supply.exception.InstanceNotFoundException;
import com.supply.message.domain.entity.ChatInformation;
import com.supply.message.utils.TimeConvertUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageConsumer {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final DiscoveryClient discoveryClient;

    private final AtomicInteger index = new AtomicInteger(0);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "finish.queue"),
            exchange = @Exchange(name = "finish.direct"),
            key = {"finishDirect"}
    ))
    public void listenFinishQueue(String message) {
        log.info("接收到finish.queue的消息：{}", message + "," + DateUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN));
        List<ServiceInstance> instances = discoveryClient.getInstances("chat-service-grpc");
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
        log.info("当前轮询到：{}：{}", host, port);
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        try {
            ChatServiceGrpc.ChatServiceBlockingStub chatServiceBlockingStub = ChatServiceGrpc.newBlockingStub(managedChannel);
            ChatProto.webSocketOnCloseRequest.Builder builder = ChatProto.webSocketOnCloseRequest.newBuilder();
            ChatProto.webSocketOnCloseRequest build = builder.setMessage(message).build();
            chatServiceBlockingStub.webSocketOnClose(build);
        } catch (Exception e) {
            log.info("webSocketOnClose远程调用失败");
            throw new RuntimeException(e);
        } finally {
            managedChannel.shutdown();
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "store.queue"),
            exchange = @Exchange(name = "store.direct"),
            key = {"storeDirect"}
    ))
    public void listenStoreQueue(String message) {
        log.info("接收到store.queue的消息：{}", message + ", " + DateUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN));
        // 反序列化为 ChatInformation 对象
        try {
            ChatInformation chatInformation = JSON.parseObject(message, ChatInformation.class);
            // 将消息存储到数据库
            List<ServiceInstance> instances = discoveryClient.getInstances("chat-service-grpc");
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
            log.info("当前轮询到：{}：{}", host, port);
            ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            try {
                ChatServiceGrpc.ChatServiceBlockingStub chatServiceBlockingStub = ChatServiceGrpc.newBlockingStub(managedChannel);
                ChatProto.storeChatInformationRequest.Builder builder = ChatProto.storeChatInformationRequest.newBuilder();
                ChatProto.storeChatInformationRequest build = builder.setId(chatInformation.getId())
                        .setQueueId(chatInformation.getQueueId())
                        .setSendUserId(chatInformation.getSendUserId())
                        .setReceiveUserId(chatInformation.getReceiveUserId())
                        .setInformation(chatInformation.getInformation())
                        .setImage(chatInformation.getImage())
                        .setSendTime(TimeConvertUtil.convertLocalDateTimeToTimestamp(chatInformation.getSendTime()))
                        .build();
                chatServiceBlockingStub.storeChatInformation(build);
            } catch (Exception e) {
                log.info("storeChatInformation远程调用失败");
                throw new RuntimeException(e);
            } finally {
                managedChannel.shutdown();
            }
            log.info("成功存储聊天信息：{}", chatInformation);
        } catch (Exception e) {
            log.error("反序列化消息失败，消息内容：{}，错误：{}", message, e.getMessage());
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "message.queue"),
            exchange = @Exchange(name = "message.direct"),
            key = {"messageDirect"}
    ))
    public void listenMessageQueue(String message) {
        log.info("接收到message.queue的消息：{}, {}", message, DateUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN));

        Object o = redisTemplate.opsForValue().get("unReadMessage:" + message);
        if (!Objects.isNull(o)) {
            String json = o.toString();
            List<JSONObject> list = JSON.parseArray(json, JSONObject.class);
            // 根据 sendTime 排序
            list = list.stream()
                    .sorted(Comparator.comparing(j -> LocalDateTime.parse(j.getString("sendTime"))))
                    .toList();
            for (JSONObject jsonObject : list) {
                Boolean isInformation = jsonObject.getBoolean("isInformation");
                Long toUserId = jsonObject.getLong("toUserId");
                Long userId = jsonObject.getLong("userId");
                String content = jsonObject.getString("content");
                LocalDateTime sendTime = LocalDateTime.parse(jsonObject.getString("sendTime"));
                List<ServiceInstance> instances = discoveryClient.getInstances("chat-service-grpc");
                if (instances == null || instances.isEmpty()) {
                    throw new InstanceNotFoundException(MessageConstant.SERVER_ERROR);
                }
                // 实现轮询
                if (index.get() > Integer.MAX_VALUE - 1000) {
                    index.set(0);
                }
                int currentIndex = index.getAndIncrement() % instances.size();
                ServiceInstance instance = instances.get(currentIndex);
                String host = instance.getHost();
                int port = instance.getPort();
                log.info("当前轮询到：{}：{}", host, port);
                ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                try {
                    ChatServiceGrpc.ChatServiceBlockingStub chatServiceBlockingStub = ChatServiceGrpc.newBlockingStub(managedChannel);
                    ChatProto.sendMessageToUserRequest build = ChatProto.sendMessageToUserRequest.newBuilder()
                            .setContent(content)
                            .setUserId(userId)
                            .setToUserId(toUserId)
                            .setIsInformation(isInformation)
                            .setSendTime(TimeConvertUtil.convertLocalDateTimeToTimestamp(sendTime))
                            .build();
                    chatServiceBlockingStub.sendMessageToUser(build);
                } catch (Exception e) {
                    log.error("sendMessageToUser远程调用失败", e);
                    throw new RuntimeException(e);
                } finally {
                    managedChannel.shutdown();
                }
            }
            redisTemplate.delete("unReadMessage:" + message);
        }
    }
}
