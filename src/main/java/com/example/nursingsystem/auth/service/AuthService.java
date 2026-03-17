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

    /**
     * 获取用户角色标识
     * @param userId 用户 ID
     * @return 角色代码（admin 或其他）
     */
    String getUserRole(Long userId);
}
