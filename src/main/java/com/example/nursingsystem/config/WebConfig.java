package com.example.nursingsystem.config;

import com.example.nursingsystem.interceptor.JwtTokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenInterceptor)
                // 拦截所有请求
                .addPathPatterns("/api/**")
                // 排除不需要认证的接口
                .excludePathPatterns(
                    "/api/auth/**",           // 认证相关接口
                    "/doc.html",              // Swagger 文档
                    "/webjars/**",            // Swagger 资源
                    "/swagger-ui/**",         // Swagger UI
                    "/v3/api-docs/**"         // API 文档
                );
    }
}
