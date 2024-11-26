package com.supply.websocket;

import com.alibaba.fastjson.JSONObject;
import com.supply.context.WebSocketContext;
import com.supply.domain.entity.ChatErrorInformation;
import com.supply.domain.entity.ChatInformation;
import com.supply.domain.entity.MessageCache;
import com.supply.mapper.ChatMapper;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ServerEndpoint(value = "/chat")
@Component
@Slf4j
public class ChatEndPoint {

    private static final Map<Long, Session> onlineUsers = new ConcurrentHashMap<>();

    private final ExecutorService retryExecutor = Executors.newCachedThreadPool();

    private static RabbitTemplate rabbitTemplate;

    private static RedisTemplate<Object, Object> redisTemplate;

    private static ChatMapper chatMapper;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        ChatEndPoint.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        ChatEndPoint.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setChatMapper(ChatMapper chatMapper) {
        ChatEndPoint.chatMapper = chatMapper;
    }

    @OnOpen
    public void onOpen(Session session) {
        Long userId = WebSocketContext.getCurrentId();
        if (userId != null) {
            // 将用户信息存入 WebSocket Session 的属性中
            session.getUserProperties().put("userId", userId);
            onlineUsers.put(userId, session);
            log.info("用户 {} 已连接", userId);
            //发送上线信息到mq
            String exchangeName = "message.direct";
            String routingKey = "messageDirect";
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend(exchangeName, routingKey, userId, correlationData);
        } else {
            log.warn("未能找到用户ID，连接未加入在线用户列表");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        Long userId = (Long) session.getUserProperties().get("userId");
        if (userId == null) {
            log.warn("未找到 session 中的用户ID，无法处理消息");
            return;
        }

        JSONObject jsonObject = JSONObject.parseObject(message);
        Long toUserId = jsonObject.getLong("to");
        String information = jsonObject.getString("information");
        String image = jsonObject.getString("image");
        String content = Optional.ofNullable(information).orElse(image);
        if (content == null) {
            log.warn("消息内容为空，无法处理");
            return;
        }
        Session toUserSession = onlineUsers.get(toUserId);
        if (toUserSession != null) {
            sendMessageToUser(content, userId, toUserId, Objects.isNull(image), LocalDateTime.now());
        } else {
            cacheOfflineMessage(content, userId, toUserId, Objects.isNull(image));
            log.warn("用户 {} 不在线，无法发送消息，进行消息缓存", toUserId);
        }
    }

    @OnClose
    public void onClose(Session session) {
        Long userId = (Long) session.getUserProperties().get("userId");
        if (userId != null) {
            onlineUsers.remove(userId);
            log.info("用户 {} 已下线", userId);
            // 发送下线信息到 RabbitMQ
            String exchangeName = "finish.direct";
            String routingKey = "finishDirect";
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend(exchangeName, routingKey, userId, correlationData);
        } else {
            log.warn("用户ID为 null，未能成功移除连接");
        }
    }

    public void sendMessageToUser(String content, Long userId, Long toUserId, Boolean isInformation, LocalDateTime now) {
        Session toUserSession = onlineUsers.get(toUserId);
        try {
            toUserSession.getBasicRemote().sendText(content);
            // 发送信息到 RabbitMQ
            Long queueId = chatMapper.getQueueByUserId(userId, toUserId);
            ChatInformation chatInformation;
            if (isInformation) {
                chatInformation = ChatInformation.builder()
                        .queueId(queueId)
                        .sendUserId(userId)
                        .receiveUserId(toUserId)
                        .sendTime(now)
                        .information(content)
                        .build();
            } else {
                chatInformation = ChatInformation.builder()
                        .queueId(queueId)
                        .sendUserId(userId)
                        .receiveUserId(toUserId)
                        .sendTime(now)
                        .image(content)
                        .build();
            }
            String exchangeName = "store.direct";
            String routingKey = "storeDirect";
            String json = JSONObject.toJSONString(chatInformation);
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend(exchangeName, routingKey, json, correlationData);
            log.info("成功发送消息给用户 {}", toUserId);
            redisTemplate.delete("queue:" + queueId);
        } catch (IOException e) {
            log.error("发送消息给用户 {} 失败: {}", toUserId, e.getMessage());
            retryExecutor.submit(() -> failureAfterSending(content, userId, toUserId, isInformation, 1));
        }
    }

    private void failureAfterSending(String content, Long userId, Long toUserId, Boolean isInformation, int retryCount) {
        if (retryCount > 3) {
            log.warn("用户 {} 消息发送失败，已重试 {} 次，进行缓存", toUserId, retryCount);
            cacheOfflineMessage(content, userId, toUserId, isInformation);
            return;
        }
        Session session = onlineUsers.get(toUserId);
        if (session == null) {
            // 用户下线，进行缓存操作
            cacheOfflineMessage(content, userId, toUserId, isInformation);
        } else {
            try {
                Thread.sleep(1000);
                log.info("重试发送消息给用户 {}, 第 {} 次", toUserId, retryCount);
                failureAfterSending(content, userId, toUserId, isInformation, retryCount + 1);
            } catch (InterruptedException e) {
                log.error("线程中断异常: {}", e.getMessage());
                Thread.currentThread().interrupt();
                cacheOfflineMessage(content, userId, toUserId, isInformation);
            }
        }
    }


    private void cacheOfflineMessage(String content, Long userId, Long toUserId, Boolean isInformation) {
        cacheOfflineMessageWithRetry(content, userId, toUserId, isInformation, 1);
    }

    private void cacheOfflineMessageWithRetry(String content, Long userId, Long toUserId, Boolean isInformation, int retryCount) {
        if (retryCount > 3) {
            log.error("缓存离线消息失败多次，用户ID: {}，执行兜底策略", toUserId);
            fallbackSaveMessage(content, userId, toUserId, isInformation);
            return;
        }

        try {
            List<MessageCache> list = (List<MessageCache>) Optional.ofNullable(redisTemplate.opsForValue().get("unReadMessage:" + toUserId))
                    .orElseGet(ArrayList::new);
            MessageCache messageCache = MessageCache.builder()
                    .isInformation(isInformation)
                    .toUserId(toUserId)
                    .content(content)
                    .userId(userId)
                    .sendTime(LocalDateTime.now())
                    .build();
            list.add(messageCache);
            redisTemplate.opsForValue().set("unReadMessage:" + toUserId, list);
            log.info("成功缓存离线消息给用户 {}", toUserId);
        } catch (Exception e) {
            log.error("缓存离线消息失败，用户ID: {}，错误: {}，重试次数: {}", toUserId, e.getMessage(), retryCount);
            retryExecutor.submit(() -> {
                try {
                    Thread.sleep(1000);
                    cacheOfflineMessageWithRetry(content, userId, toUserId, isInformation, retryCount + 1);
                } catch (InterruptedException ex) {
                    log.error("线程异常: {}", ex.getMessage());
                    Thread.currentThread().interrupt();
                    fallbackSaveMessage(content, userId, toUserId, isInformation);
                }
            });
        }
    }

    private void fallbackSaveMessage(String content, Long userId, Long toUserId, Boolean isInformation) {
        Long queueId = chatMapper.getQueueByUserId(userId, toUserId);
        ChatErrorInformation chatErrorInformation;
        if (isInformation) {
            chatErrorInformation = ChatErrorInformation.builder()
                    .queueId(queueId)
                    .sendUserId(userId)
                    .receiveUserId(toUserId)
                    .information(content)
                    .build();
        } else {
            chatErrorInformation = ChatErrorInformation.builder()
                    .queueId(queueId)
                    .sendUserId(userId)
                    .receiveUserId(toUserId)
                    .image(content)
                    .build();
        }
        chatMapper.storeErrorMessage(chatErrorInformation);
    }

}