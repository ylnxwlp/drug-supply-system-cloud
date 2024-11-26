package com.supply.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.supply.api.user.UserProto;
import com.supply.api.user.UserServiceGrpc;
import com.supply.constant.MessageConstant;
import com.supply.domain.dto.PageQueryDTO;
import com.supply.domain.entity.*;
import com.supply.domain.vo.ReportInformationVO;
import com.supply.domain.vo.UserInformationVO;
import com.supply.domain.vo.VerificationInformationVO;
import com.supply.enumeration.EmailType;
import com.supply.exception.InstanceNotFoundException;
import com.supply.mapper.AdminMapper;
import com.supply.result.PageResult;
import com.supply.result.Result;
import com.supply.service.AdminService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
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
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final RabbitTemplate rabbitTemplate;

    private final DiscoveryClient discoveryClient;

    private final AtomicInteger index = new AtomicInteger(0);
    /**
     * 个人信息回显
     *
     * @return 用户信息
     */
    public UserInformationVO getInformation() {
        Long userId = getCurrentUserId();
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service-grpc");
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
        UserProto.authenticationResponse codeResponse;
        try {
            UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel);
            UserProto.authorityRequest.Builder builder = UserProto.authorityRequest.newBuilder();
            UserProto.authorityRequest re = builder.setId(userId).build();
            //完成RPC调用
            codeResponse = userServiceBlockingStub.getUserInformation(re);
            log.info("调用用户信息返回结果：{}", codeResponse);
            UserInformationVO userInformationVO = new UserInformationVO();
            BeanUtils.copyProperties(codeResponse, userInformationVO);
            log.debug("当前登录的管理员信息：{}", userInformationVO);
            return userInformationVO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            managedChannel.shutdown();
        }
    }

    /**
     * 申请认证信息查询
     *
     * @param type 工种编号，1为医护端，2为供应端
     * @return 待审核的身份信息
     */
    public List<VerificationInformationVO> getVerificationInformation(Long type) {
        String cacheKey = "VerificationInformation:" + type;
        List<VerificationInformationVO> list = null;
        try {
            list = (List<VerificationInformationVO>) redisTemplate.opsForValue().get(cacheKey);
        } catch (Exception e) {
            log.error("getVerificationInformation方法中redis反序列化异常");
        }
        if (list != null) {
            return list;
        }
        List<IdentityAuthentication> verificationInformation = adminMapper.getVerificationInformation(type);
        log.debug("工种编号为{}的申请信息：{}", type, verificationInformation);
        list = verificationInformation.stream().map(info -> {
            VerificationInformationVO vo = new VerificationInformationVO();
            BeanUtils.copyProperties(info, vo);
            vo.setApplicationTime(DateUtil.format(info.getApplicationTime(), DatePattern.NORM_DATETIME_PATTERN));
            Result<User> result = userClient.getUserInformationBuId(info.getUserId());
            User user = result.getData();
            vo.setUsername(user.getUsername());
            vo.setFirmName(user.getFirmName());
            vo.setImages(Arrays.asList(info.getImages().split(",")));
            log.debug("查询到的数据信息：{}", vo);
            return vo;
        }).collect(Collectors.toList());
        if (!list.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, list, 55 + RandomUtil.randomInt(10), TimeUnit.MINUTES);
        } else {
            log.debug("工种编号{}下暂时没有新的申请信息", type);
        }
        return list;
    }

    /**
     * 身份信息审核接口
     *
     * @param id      身份信息申请id
     * @param isAgree 是否同意，1为是，2为否
     */
    public void checkVerificationInformation(Long id, Long isAgree) {
        log.debug("管理员对于申请编号为{}的认证做出决定：{}", id, isAgree);
        Long applyUserId = adminMapper.getApplyUser(id);
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service-grpc");
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
        UserProto.authenticationResponse codeResponse;
        try {
            UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel);
            UserProto.authorityRequest.Builder builder = UserProto.authorityRequest.newBuilder();
            UserProto.authorityRequest re = builder.setId(applyUserId).build();
            //完成RPC调用
            codeResponse = userServiceBlockingStub.getUserInformation(re);
            log.info("调用用户信息返回结果：{}", codeResponse);
            String email = codeResponse.getEmail();
            Long adminId = getCurrentUserId();
            if (isAgree == 1) {
                adminMapper.checkSuccessfully(id, adminId, LocalDateTime.now());
                userClient.checkSuccessfully(applyUserId,LocalDateTime.now(),codeResponse.getWorkType());
                redisTemplate.delete("allUsers");
                String jsonString = JSON.toJSONString(EmailMessage.builder()
                        .emailAddress(email)
                        .emailType(EmailType.CHECK_SUCCESS.toString())
                        .adminId(adminId)
                        .build());
                CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("email.direct", "emailDirect", jsonString, correlationData);
            } else {
                userClient.checkFail(applyUserId, LocalDateTime.now());
                adminMapper.checkUnsuccessfully(id, adminId);
            }
            redisTemplate.delete("VerificationInformation:" + codeResponse.getWorkType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            managedChannel.shutdown();
        }
    }

    /**
     * 举报信息查询接口
     *
     * @return 举报信息
     */
    public List<ReportInformationVO> getReportInformation() {
        List<ReportInformationVO> list = null;
        try {
            list = (List<ReportInformationVO>) redisTemplate.opsForValue().get("report");
        } catch (Exception e) {
            log.error("getReportInformation方法中redis反序列化异常");
        }
        if (list != null) {
            return list;
        }
        log.debug("查询所有举报信息");
        List<Report> reports = adminMapper.getAllReportInformation();
        list = reports.stream().map(report -> {
            ReportInformationVO vo = new ReportInformationVO();
            BeanUtils.copyProperties(report, vo);
            vo.setReportTime(DateUtil.format(report.getReportTime(), DatePattern.NORM_DATETIME_PATTERN));
            vo.setImages(Arrays.asList(report.getImages().split(",")));
            vo.setFirmName(userClient.getUserInformationBuId(report.getUserId()).getData().getFirmName());
            vo.setInformerFirmName(userClient.getUserInformationBuId(report.getUserId()).getData().getFirmName());
            return vo;
        }).collect(Collectors.toList());

        if (!list.isEmpty()) {
            redisTemplate.opsForValue().set("report", list, 175 + RandomUtil.randomInt(10), TimeUnit.MINUTES);
        }
        return list;
    }

    /**
     * 处理举报信息
     *
     * @param id        举报id
     * @param isIllegal 是否违规，1为是，2为否
     * @param isBlocked 是否进行封禁处理
     */
    public void dealReport(Long id, Integer isIllegal, Integer isBlocked) {
        log.debug("管理员处理举报id：{}，违规标志：{}", id, isIllegal);
        Report report = adminMapper.getReportInformation(id);
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service-grpc");
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
        UserProto.authenticationResponse codeResponse;
        try {
            UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel);
            UserProto.authorityRequest.Builder builder = UserProto.authorityRequest.newBuilder();
            UserProto.authorityRequest re = builder.setId(report.getReportUserId()).build();
            //完成RPC调用
            codeResponse = userServiceBlockingStub.getUserInformation(re);
            log.info("调用用户信息返回结果：{}", codeResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            managedChannel.shutdown();
        }
        String reportUserEmail = codeResponse.getEmail();
        String userEmail = codeResponse.getEmail();
        adminMapper.dealReport(id);
        if (isIllegal == 1 && isBlocked == 2) {
            //向mq发送消息
            String jsonString = JSON.toJSONString(EmailMessage.builder()
                    .emailAddress(reportUserEmail)
                    .anotherEmailAddress(userEmail)
                    .emailType(EmailType.REPORT_BLOCKED.toString()));
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend("email.direct", "emailDirect", jsonString, correlationData);
        } else if (isIllegal == 1 && isBlocked == 1) {
            //将被举报人的账户封禁
            userClient.blockAccount(report.getUserId(), LocalDateTime.now());
            //向mq发送消息
            String jsonString = JSON.toJSONString(EmailMessage.builder()
                    .emailAddress(reportUserEmail)
                    .anotherEmailAddress(userEmail)
                    .emailType(EmailType.REPORT_BLOCKED.toString()));
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend("email.direct", "emailDirect", jsonString, correlationData);
        } else {
            //向mq发送消息
            String jsonString = JSON.toJSONString(EmailMessage.builder()
                    .emailAddress(reportUserEmail)
                    .emailType(EmailType.REPORT_BLOCKED.toString()));
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend("email.direct", "emailDirect", jsonString, correlationData);
        }
        redisTemplate.delete("report");
    }

    /**
     * 获取所有通过认证的用户信息
     *
     * @return 用户信息
     */
    public PageResult getAllUsers(PageQueryDTO pageQueryDTO) {
        List<UserInformationVO> list = null;
        try {
            list = (List<UserInformationVO>) redisTemplate.opsForValue().get("allUsers");
        } catch (Exception e) {
            log.error("getAllUsers方法中redis反序列化异常");
        }
        if (list != null) {
            log.debug("缓存中的用户信息：{}", list);
            return new PageResult(list.size(), paginateList(list, pageQueryDTO.getPage(), pageQueryDTO.getPageSize()));
        }
        log.debug("查询所有用户信息");
        List<User> users = userClient.getAllUsers().getData();
        list = users.stream().map(user -> {
            UserInformationVO vo = new UserInformationVO();
            BeanUtils.copyProperties(user, vo);
            return vo;
        }).collect(Collectors.toList());
        if (!list.isEmpty()) {
            redisTemplate.opsForValue().set("allUsers", list, 25 + RandomUtil.randomInt(10), TimeUnit.MINUTES);
        }
        return new PageResult(list.size(), paginateList(list, pageQueryDTO.getPage(), pageQueryDTO.getPageSize()));
    }

    /**
     * 封禁用户
     *
     * @param id 用户id
     */
    public void block(Long id) {
        userClient.blockAccount(id, LocalDateTime.now());
    }

    /**
     * 解封用户
     *
     * @param id 用户id
     */
    public void liftUser(Long id) {
        userClient.lift(id,LocalDateTime.now());
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
     * 添加认证信息
     * @param identityAuthentication 认证信息
     */
    public void addVerificationMessage(IdentityAuthentication identityAuthentication) {
        adminMapper.addVerificationMessage(identityAuthentication);
    }

    /**
     * 分页查询截取
     *
     * @param list     截取集合
     * @param page     起始页面
     * @param pageSize 页面数据数
     * @return 截取后的集合
     */
    private List<UserInformationVO> paginateList(List<UserInformationVO> list, int page, int pageSize) {
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, list.size());
        if (fromIndex >= list.size()) {
            return new ArrayList<>();
        }
        return list.subList(fromIndex, toIndex);
    }
}
