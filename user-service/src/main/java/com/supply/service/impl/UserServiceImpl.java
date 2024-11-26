package com.supply.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.supply.api.message.MessageProto;
import com.supply.api.message.MessageServiceGrpc;
import com.supply.api.user.UserProto;
import com.supply.api.user.UserServiceGrpc;
import com.supply.domain.dto.UserInformationDTO;
import com.supply.domain.entity.IdentityAuthentication;
import com.supply.domain.entity.LoginUser;
import com.supply.domain.entity.User;
import com.supply.domain.vo.UserInformationVO;
import com.supply.exception.InstanceNotFoundException;
import com.supply.mapper.UserMapper;
import com.supply.service.UserService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.supply.constant.MessageConstant;
import com.supply.exception.VerificationCodeErrorException;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final UserMapper userMapper;

    private final DiscoveryClient discoveryClient;

    private final AtomicInteger index = new AtomicInteger(0);

    private final RabbitTemplate rabbitTemplate;

    /**
     * gRPC远程调用获取用户信息
     *
     * @param request          请求内容
     * @param responseObserver 响应封装
     */
    public void getUserInformationById(UserProto.authenticationRequest request, StreamObserver<UserProto.authenticationResponse> responseObserver) {
        User u;
        if (request.getUsername().isEmpty()) {
            u = User.builder()
                    .firmName(request.getFirmName())
                    .email(request.getEmail()).build();
        } else {
            u = User.builder()
                    .firmName(request.getFirmName())
                    .username(request.getUsername())
                    .build();
        }
        User user = userMapper.login(u);
        UserProto.authenticationResponse authenticationResponse = packUserInformation(user);
        log.info("完成调用信息的封装");
        responseObserver.onNext(authenticationResponse);
        responseObserver.onCompleted();
    }

    /**
     * gRPC远程调用获取用户权限信息
     *
     * @param request          请求内容
     * @param responseObserver 响应封装
     */
    public void getUserAuthority(UserProto.authorityRequest request, StreamObserver<UserProto.authorityResponse> responseObserver) {
        List<String> authority = userMapper.getAuthority(request.getId());
        UserProto.authorityResponse.Builder builder = UserProto.authorityResponse.newBuilder();
        builder.addAllAuthorities(authority);
        UserProto.authorityResponse build = builder.build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }

    /**
     * gRPC远程调用根据用户id获取用户信息
     *
     * @param request          请求内容
     * @param responseObserver 响应封装
     */
    public void getUserInformation(UserProto.authorityRequest request, StreamObserver<UserProto.authenticationResponse> responseObserver) {
        User information = userMapper.getUserInformationById(request.getId());
        UserProto.authenticationResponse authenticationResponse = packUserInformation(information);
        responseObserver.onNext(authenticationResponse);
        responseObserver.onCompleted();
    }

    private UserProto.authenticationResponse packUserInformation(User information) {
        UserProto.authenticationResponse.Builder builder = UserProto.authenticationResponse.newBuilder();
        builder.setId(information.getId())
                .setUsername(information.getUsername())
                .setFirmName(information.getFirmName())
                .setWorkType(information.getWorkType())
                .setImage(information.getImage())
                .setEmail(information.getEmail())
                .setAccountStatus(information.getAccountStatus())
                .setTelephone(information.getTelephone())
                .setPassword(information.getPassword())
                .setResume(information.getResume())
                .setCreateTime(DateUtil.format(information.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN))
                .setUpdateTime(DateUtil.format(information.getUpdateTime(), DatePattern.NORM_DATETIME_PATTERN))
                .setAge(information.getAge())
                .setIdNumber(information.getIdNumber());
        return builder.build();
    }

    /**
     * 用户注册
     *
     * @param userInformationDTO 用户注册信息
     */
    @GlobalTransactional
    public void register(UserInformationDTO userInformationDTO) {
        String code = (String) redisTemplate.opsForValue().get("register:" + userInformationDTO.getEmail());
        if (!Objects.equals(code, userInformationDTO.getVerifyCode())) {
            throw new VerificationCodeErrorException(MessageConstant.VERIFICATION_CODE_ERROR);
        }
        //进行User实体类的封装
        User user = new User();
        BeanUtils.copyProperties(userInformationDTO, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userInformationDTO.getIdentity() == 1) {
            user.setWorkType(1);
            user.setImage("https://pro11.oss-cn-hangzhou.aliyuncs.com/DALL%C2%B7E%202024-09-01%2021.42.24%20-%20A%20cartoon%20illustration%20of%20a%20healthcare%20professional.%20The%20character%20is%20wearing%20a%20white%20lab%20coat%2C%20a%20stethoscope%20around%20their%20neck%2C%20and%20a%20friendly%20smile.webp");
        } else {
            user.setWorkType(2);
            user.setImage("https://pro11.oss-cn-hangzhou.aliyuncs.com/DALL%C2%B7E%202024-09-01%2021.50.49%20-%20A%20cartoon%20illustration%20of%20a%20pharmaceutical%20supplier.%20The%20character%20is%20wearing%20a%20white%20lab%20coat%2C%20glasses%2C%20and%20is%20holding%20a%20box%20labeled%20%27Medicines.%27%20The.webp");
        }
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        log.info("用户信息：{}", user);
        userMapper.register(user);
        sendVerificationMessage(userInformationDTO, user);
        //删除缓存数据
        redisTemplate.delete("VerificationInformation:" + userInformationDTO.getIdentity());
    }

    /**
     * 邮箱验证码保存
     *
     * @param email         注册的邮箱
     * @param operationType 操作类型，1为注册，2为重置密码
     */
    public void sendCode(String email, Long operationType) {
        List<ServiceInstance> instances = discoveryClient.getInstances("message-service-grpc");
        if (instances == null || instances.isEmpty()) {
            throw new InstanceNotFoundException(MessageConstant.SERVER_ERROR);
        }// 实现轮询
        if (index.get() > Integer.MAX_VALUE - 1000) {
            index.set(0);
        }
        int currentIndex = index.getAndIncrement() % instances.size();
        URI uri = instances.get(currentIndex).getUri();
        String host = uri.getHost();
        int port = uri.getPort();
        log.info("当前轮询到：{}", host);
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        MessageProto.codeResponse codeResponse;
        try {
            MessageServiceGrpc.MessageServiceBlockingStub messageServiceBlockingStub = MessageServiceGrpc.newBlockingStub(managedChannel);
            MessageProto.codeRequest.Builder builder = MessageProto.codeRequest.newBuilder();
            MessageProto.codeRequest re = builder.setEmail(email).build();
            //完成RPC调用
            codeResponse = messageServiceBlockingStub.code(re);
            log.info("调用用户信息返回结果：{}", codeResponse);
            String code = codeResponse.getCode();
            if (operationType.equals(1L)) {
                log.info("邮箱{}注册的验证码为：{}", email, code);
                redisTemplate.opsForValue().set("register:" + email, code, 5, TimeUnit.MINUTES);
            } else {
                log.info("邮箱{}重置密码的验证码为：{}", email, code);
                redisTemplate.opsForValue().set("resetPassword:" + email, code, 5, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            managedChannel.shutdown();
        }
    }

    /**
     * 重置密码
     *
     * @param userInformationDTO 用户信息
     */
    public void resetPassword(UserInformationDTO userInformationDTO) {
        String code = (String) redisTemplate.opsForValue().get(userInformationDTO.getEmail());
        if (!Objects.equals(code, userInformationDTO.getVerifyCode())) {
            throw new VerificationCodeErrorException(MessageConstant.VERIFICATION_CODE_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userInformationDTO, user);
        user.setPassword(passwordEncoder.encode(userInformationDTO.getPassword()));
        log.info("用户更改的信息：{}", user);
        userMapper.resetPassword(user, LocalDateTime.now());
    }

    /**
     * 登出接口
     */
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        redisTemplate.delete("login:" + userId);
    }

    /**
     * 重新上传身份文件接口
     *
     * @param userInformationDTO 新身份证明文件
     */
    public void ReUploadIdentityFile(UserInformationDTO userInformationDTO) {
        User user = new User();
        if (!userInformationDTO.getUsername().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            user.setUsername(userInformationDTO.getUsername());
        } else {
            user.setEmail(userInformationDTO.getUsername());
        }
        User u = userMapper.login(user);
        sendVerificationMessage(userInformationDTO, u);
    }

    /**
     * @param userInformationDTO 用户认证信息
     * @param u                  用户信息
     */
    private void sendVerificationMessage(UserInformationDTO userInformationDTO, User u) {
        List<String> verificationImages = userInformationDTO.getVerificationImages();
        String images = String.join(",", verificationImages);
        IdentityAuthentication identityAuthentication = IdentityAuthentication.builder()
                .applicationTime(LocalDateTime.now())
                .images(images)
                .idNumber(u.getIdNumber())
                .userId(u.getId())
                .workType(u.getWorkType())
                .build();
        String jsonString = JSON.toJSONString(identityAuthentication);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("admin.direct", "adminDirect", jsonString, correlationData);
    }

    /**
     * 修改个人信息回显
     *
     * @return 个人信息
     */
    public UserInformationVO getModificationInformation() {
        Long userId = getCurrentUserId();
        User user = userMapper.getUserInformationById(userId);
        UserInformationVO userInformationVO = new UserInformationVO();
        BeanUtils.copyProperties(user, userInformationVO);
        return userInformationVO;
    }

    /**
     * 获取当前登录用户的ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getUser().getId();
    }

    /**
     * 修改个人信息
     *
     * @param userInformationDTO 新信息
     */
    public void updateUserInformation(UserInformationDTO userInformationDTO) {
        Long userId = getCurrentUserId();
        log.info("修改用户个人信息：{}", userInformationDTO);
        new User();
        User user = User.builder()
                .id(userId)
                .firmName(userInformationDTO.getFirmName())
                .email(userInformationDTO.getEmail())
                .telephone(userInformationDTO.getTelephone())
                .updateTime(LocalDateTime.now())
                .build();
        userMapper.updateUserInformation(user);
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    public User getUserInformationById(Long id) {
        return userMapper.getUserInformationById(id);
    }

    /**
     * 审核成功
     *
     * @param id            用户id
     * @param localDateTime 审核时间
     * @param workType      工种
     */
    @Transactional
    public void checkSuccessfully(Long id, LocalDateTime localDateTime, Integer workType) {
        userMapper.changeStatusToNormal(id, LocalDateTime.now());
        userMapper.setAuthority(id, workType);
    }

    /**
     * 审核失败
     *
     * @param id            用户id
     * @param localDateTime 审核时间
     */
    public void checkFail(Long id, LocalDateTime localDateTime) {
        userMapper.changeStatusToCheckFailed(id, localDateTime);
    }

    /**
     * 封禁用户
     *
     * @param id            用户id
     * @param localDateTime 封禁时间
     */
    public void blockAccount(Long id, LocalDateTime localDateTime) {
        userMapper.blockAccount(id, localDateTime);
    }

    /**
     * 解禁用户
     *
     * @param id            用户id
     * @param localDateTime 解禁时间
     */
    public void lift(Long id, LocalDateTime localDateTime) {
        userMapper.liftUser(id, localDateTime);
    }

    /**
     * 获取所有用户
     *
     * @return 所有用户信息
     */
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }
}
