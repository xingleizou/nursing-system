package com.example.nursingsystem.interceptor;

import com.example.nursingsystem.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * JWT 令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    /**
     * 校验 JWT
     *
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @return 是否放行
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前拦截到的是 Controller 的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            // 当前拦截到的不是动态方法，直接放行
            log.debug("非 Controller 方法，直接放行：{}", request.getRequestURI());
            return true;
        }

        String requestURI = request.getRequestURI();
        log.info("=== JWT 拦截器拦截请求：{} ===", requestURI);

        // 1、从请求头中获取令牌
        String token = request.getHeader("Authorization");
        log.info("请求头 Authorization: {}", token != null ? "已设置" : "未设置");

        // 2、校验令牌
        try {
            if (token == null || token.isEmpty()) {
                log.warn("token 为空，返回 401");
                response.setStatus(401);
                return false;
            }

            // 如果 token 以 Bearer 开头，则去掉前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            log.info("jwt 校验 (已去前缀):{}", token);

            // 验证 token
            if (!JwtUtil.verifyToken(token)) {
                log.error("JWT 验证失败");
                response.setStatus(401);
                return false;
            }

            // 解析 token 获取用户信息
            Map<String, Object> claims = JwtUtil.parseToken(token);
            if (claims != null) {
                String userId = claims.get("userId").toString();
                log.info("当前用户 id：{}", userId);
                
                // 将用户 ID 存入请求属性，供后续使用
                request.setAttribute("userId", userId);
            }
            
            // 3、通过，放行
            return true;
        } catch (Exception ex) {
            log.error("JWT 校验失败：{}", ex.getMessage());
            // 4、不通过，响应 401 状态码
            response.setStatus(401);
            return false;
        }
    }
}
