package com.supply.consumer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supply.domain.entity.IdentityAuthentication;
import com.supply.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailConsumer {

    private AdminService adminService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "admin.queue"),
            exchange = @Exchange(name = "admin.direct"),
            key = {"adminDirect"}
    ))
    public void listenAdminDirect(String message) {
        IdentityAuthentication identityAuthentication = JSONObject.parseObject(message, IdentityAuthentication.class);
        adminService.addVerificationMessage(identityAuthentication);
    }
}
