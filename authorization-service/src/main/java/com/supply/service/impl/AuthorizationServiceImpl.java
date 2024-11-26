package com.supply.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supply.constant.JwtClaimsConstant;
import com.supply.constant.MessageConstant;
import com.supply.domain.dto.UserLoginDTO;
import com.supply.domain.entity.EmailMessage;
import com.supply.domain.entity.LoginUser;
import com.supply.domain.entity.User;
import com.supply.domain.vo.UserLoginVO;
import com.supply.enumeration.EmailType;
import com.supply.exception.AccountStatusException;
import com.supply.exception.LoginErrorException;
import com.supply.properties.JwtProperties;
import com.supply.service.AuthorizationService;
import com.supply.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtProperties jwtProperties;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final RabbitTemplate rabbitTemplate;

    private final ExecutorService retryExecutor = Executors.newCachedThreadPool();

    @Value("${drug.locationKey.key}")
    private String key;

    /**
     * 授权
     *
     * @param userLoginDTO 用户登录信息
     * @param request      发起请求信息
     * @return 登录返回信息
     */
    public UserLoginVO authorization(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        //AuthenticationManager进行用户认证
        Authentication authenticate = getAuthentication(userLoginDTO);
        //在Authentication中获取用户信息
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        User u = loginUser.getUser();
        if (u != null && u.getAccountStatus() == 1) {
            UserLoginVO userLoginVO = UserLoginVO.builder()
                    .id(u.getId())
                    .build();
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.ID, userLoginVO.getId());
            String jwt = JwtUtil.createJWT(jwtProperties.getSecretKey(), jwtProperties.getTtl(), claims);
            userLoginVO.setToken(jwt);
            String userId = u.getId().toString();
            //用户信息存入redis
            redisTemplate.opsForValue().set("login:" + userId, loginUser);
            retryExecutor.submit(() -> {            //给用户发送邮件进行提醒
                //先获取用户当前IP
                String ip = request.getRemoteAddr();
                CloseableHttpClient aDefault = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet("https://restapi.amap.com/v3/ip?ip=" + ip + "&key=" + key);
                String location = "北京市";
                try {
                    CloseableHttpResponse execute = aDefault.execute(httpGet);
                    HttpEntity entity = execute.getEntity();
                    String IP = EntityUtils.toString(entity);
                    JSONObject jsonObject = JSONObject.parseObject(IP);
                    location = jsonObject.getString("province") + jsonObject.getString("city");
                    log.info("当前用户登录地址：{}", location);
                    execute.close();
                    aDefault.close();
                } catch (IOException e) {
                    log.error("获取登录位置失败");
                }
                //再发送消息到交换机异步发送邮件进行提醒
                String jsonString = JSON.toJSONString(EmailMessage.builder()
                        .emailType(EmailType.LOGIN.toString())
                        .emailAddress(u.getEmail())
                        .location(location)
                        .build());
                CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("email.direct", "emailDirect", jsonString, correlationData);
            });
            return UserLoginVO.builder()
                    .token(jwt)
                    .id(Long.valueOf(userId))
                    .build();
        } else if (u != null && u.getAccountStatus() == 2) {
            throw new AccountStatusException(MessageConstant.ACCOUNT_LOCKED);
        } else if (u != null && u.getAccountStatus() == 3) {
            throw new AccountStatusException(MessageConstant.ACCOUNT_PREPARING);
        } else {
            throw new AccountStatusException(MessageConstant.ACCOUNT_PREPARE_FAILED);
        }
    }

    /**
     * 认证
     *
     * @param userLoginDTO 用户登录信息
     * @return 用户认证结果
     */
    private Authentication getAuthentication(UserLoginDTO userLoginDTO) {
        AuthenticationManager authenticationManager;
        try {
            authenticationManager = authenticationConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        DrugUsernameFirmNameAuthenticationToken drugUsernameFirmAuthenticationToken = new DrugUsernameFirmNameAuthenticationToken(userLoginDTO.getUsernameOrEmail(), userLoginDTO.getFirmName(), userLoginDTO.getPassword());
        Authentication authenticate;
        //如果认证没通过，给出对应提示
        try {
            authenticate = authenticationManager.authenticate(drugUsernameFirmAuthenticationToken);
        } catch (AuthenticationException e) {
            throw new LoginErrorException(MessageConstant.INFORMATION_ERROR);
        }
        return authenticate;
    }
}
