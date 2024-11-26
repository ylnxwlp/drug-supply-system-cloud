package com.supply.message.consumer;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.supply.api.seckill.SeckillProto;
import com.supply.api.seckill.SeckillServiceGrpc;
import com.supply.constant.MessageConstant;
import com.supply.exception.InstanceNotFoundException;
import com.supply.message.domain.entity.FlashSaleInformation;
import com.supply.message.utils.TimeConvertUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
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
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Component
public class FlashSaleConsumer {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final DiscoveryClient discoveryClient;

    private final AtomicInteger index = new AtomicInteger(0);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "flashSale.queue"),
            exchange = @Exchange(name = "flashSale.direct"),
            key = {"flashSaleDirect"}
    ))
    public void listenFinishQueue(Message message) {
        String messageId = message.getMessageProperties().getMessageId();
        String messageBody = new String(message.getBody());
        log.info("flashSale.queue的消息ID：{}, 消息内容：{}, 时间：{}",
                messageId, messageBody, DateUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN));
        // 持久化信息进数据库
        FlashSaleInformation flashSaleInformation = JSON.parseObject(messageBody, FlashSaleInformation.class);
        Object o = redisTemplate.opsForValue().get("message:" + messageId);
        if (o == null) {
            //未被消费，直接进行持久化
            List<ServiceInstance> instances = discoveryClient.getInstances("seckill-service-grpc");
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
            try {
                SeckillServiceGrpc.SeckillServiceBlockingStub seckillServiceBlockingStub = SeckillServiceGrpc.newBlockingStub(managedChannel);
                SeckillProto.StoreFlashSaleInformationRequest.Builder builder = SeckillProto.StoreFlashSaleInformationRequest.newBuilder();
                builder.setFlashSaleDrugId(flashSaleInformation.getId())
                        .setOrderNumber(flashSaleInformation.getOrderNumber().toString())
                        .setUserId(flashSaleInformation.getUserId())
                        .setStatus(2)
                        .setOrderTime(TimeConvertUtil.convertLocalDateTimeToTimestamp(flashSaleInformation.getOrderTime(), "Asia/Shanghai"))
                        .build();
                SeckillProto.StoreFlashSaleInformationRequest build = builder.build();
                seckillServiceBlockingStub.storeFlashSaleInformation(build);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                SeckillServiceGrpc.SeckillServiceBlockingStub seckillServiceBlockingStub = SeckillServiceGrpc.newBlockingStub(managedChannel);
                SeckillProto.InventoryDeductionRequest.Builder builder = SeckillProto.InventoryDeductionRequest.newBuilder();
                SeckillProto.InventoryDeductionRequest build = builder.setId(flashSaleInformation.getId()).build();
                seckillServiceBlockingStub.inventoryDeduction(build);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }finally {
                managedChannel.shutdown();
            }
            redisTemplate.opsForValue().set("message:" + messageId, 1, flashSaleInformation.getTimeOfDuration() + 5L, TimeUnit.MINUTES);
        }
    }


}
