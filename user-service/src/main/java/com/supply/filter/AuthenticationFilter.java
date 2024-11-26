package com.supply.filter;

import com.alibaba.fastjson.JSON;
import com.supply.domain.entity.LoginUser;
import com.supply.properties.AuthProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final AuthProperties authProperties;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求路径
        String requestPath = request.getRequestURI();
        // 如果请求路径在白名单中，则直接放行
        if (authProperties.getExcludePaths().contains(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 获取 token
        String userId = request.getHeader("user-info");
        // 从 Redis 中获取用户信息
        LoginUser loginUser;
        Object jsonData = redisTemplate.opsForValue().get("login:" + userId);
        // 检查是否从 Redis 获取到数据
        if (Objects.isNull(jsonData)) {
            log.warn("Redis 中不存在该用户的登录信息, userId: {}", userId);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED); // 返回401未授权
            response.getWriter().write("用户未登录或会话已过期");
            return;
        }
        try {
            loginUser = JSON.parseObject((String) jsonData, LoginUser.class);
        } catch (Exception e) {
            log.error("解析用户登录信息时出现异常, userId: {}, 错误信息: {}", userId, e.getMessage());
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR); // 返回500服务器错误
            response.getWriter().write("服务器解析用户信息时出错");
            return;
        }
        // 检查解析后的 loginUser 是否为 null
        if (Objects.isNull(loginUser)) {
            log.warn("用户未登录或会话已过期, userId: {}", userId);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED); // 返回401未授权
            response.getWriter().write("用户未登录或会话已过期");
            return;
        }
        // 存入 SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 日志记录
        log.info("用户 {} 请求资源", userId);

        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }
}
