package com.supply.filter;

import com.supply.config.AuthProperties;
import com.supply.properties.JwtProperties;
import com.supply.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final JwtProperties jwtProperties;

    private final AuthProperties authProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("开始进行网关校验");
        ServerHttpRequest request = exchange.getRequest();
        // 检查是否已经重定向过（避免重复执行校验）
        if (exchange.getRequest().getHeaders().containsKey("Redirected")) {
            log.info("检测到重定向标识，直接放行");
            return chain.filter(exchange);
        }
        // 判断是否不需要拦截
        if (isExclude(request.getPath().toString())) {
            log.info("无需拦截请求，直接放行");
            return chain.filter(exchange);
        }

        // 获取请求头中的token
        String token;
        List<String> headers = request.getHeaders().get("authorization");
        if (headers != null && !headers.isEmpty()) {
            token = headers.get(0);
        } else {
            log.info("没有token！");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().set("Location", "/authorization");
            response.getHeaders().add("Redirected", "true");  // 添加重定向标识
            return response.setComplete();
        }
        // 校验并解析token
        String userId;
        try {
            userId = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token).get("id").toString();
            log.info("用户id为：{}", userId);
        } catch (Exception e) {
            log.info("解析令牌失败！");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().set("Location", "/authorization");
            return response.setComplete();
        }

        // 传递用户信息
        ServerWebExchange serverWebExchange = exchange.mutate()
                .request(builder -> builder.header("user-info", userId))
                .build();

        return chain.filter(serverWebExchange);
    }

    private boolean isExclude(String antPath) {
        for (String pathPattern : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pathPattern, antPath)) {
                return true;
            }
        }
        return false;
    }

    public int getOrder() {
        return 0;
    }
}
