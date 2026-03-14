package com.example.nursingsystem.auth.service.impl;

import com.example.nursingsystem.auth.service.AuthService;
import com.example.nursingsystem.common.exception.BusinessException;
import com.example.nursingsystem.common.exception.ErrorCode;
import com.example.nursingsystem.common.utils.JwtUtil;
import com.example.nursingsystem.system.entity.User;
import com.example.nursingsystem.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        // 查询用户
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_USER_NOT_EXIST.getCode(),
                                        ErrorCode.LOGIN_USER_NOT_EXIST.getMessage());
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_PASSWORD_ERROR.getCode(),
                                        ErrorCode.LOGIN_PASSWORD_ERROR.getMessage());
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.USER_STATUS_DISABLED.getCode(),
                                        ErrorCode.USER_STATUS_DISABLED.getMessage());
        }

        // 生成 Token
        return JwtUtil.createToken(user.getUserId().toString(), user.getUsername());
    }

    @Override
    public void logout() {
        // TODO: 可以实现 Token 黑名单机制
    }

    @Override
    public boolean verifyToken(String token) {
        return JwtUtil.verifyToken(token);
    }
}
