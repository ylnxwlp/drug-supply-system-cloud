package com.supply.routes;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

@Component
@Slf4j
@RequiredArgsConstructor
public class DynamicRouteLoader {

    private final NacosConfigManager nacosConfigManager;

    private final RouteDefinitionWriter routeDefinitionWriter;

    private final Set<String> routeIds = new HashSet<>();//用于存储上一次的路由信息

    @PostConstruct//用于项目构建时执行该方法
    public void initRouteConfigListener() throws NacosException {
        //1、项目启动先拉去一次配置，并配置监听器
        String dataId = "gateway-routes.json";
        String group = "DEFAULT_GROUP";
        String configAndSignListener = nacosConfigManager.getConfigService()
                .getConfigAndSignListener(dataId, group, 5000, new Listener() {
                    public Executor getExecutor() {
                        return null;
                    }
                    public void receiveConfigInfo(String s) {
                        //2、监听到配置变更，更新路由表
                        updateConfigInfo(s);
                    }
                });
        //3、第一次读取也需要更新路由表
        updateConfigInfo(configAndSignListener);
    }

    public void updateConfigInfo(String configInfo){
        log.debug("监听到路由信息：{}",configInfo);
        //1、解析配置文件
        List<RouteDefinition> list = JSONUtil.toList(configInfo, RouteDefinition.class);
        //2、删除并更新路由表
        for (String routeId : routeIds) {
            routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
        }
        routeIds.clear();
        for (RouteDefinition routeDefinition : list) {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            //记录路由id，便于下一次更新删除
            routeIds.add(routeDefinition.getId());
        }
    }
}