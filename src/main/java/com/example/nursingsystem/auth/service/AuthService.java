package com.example.nursingsystem.auth.service;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     */
    String login(String username, String password);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 验证 Token
     */
    boolean verifyToken(String token);
}
