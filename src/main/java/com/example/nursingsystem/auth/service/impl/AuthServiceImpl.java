package com.example.nursingsystem.auth.service.impl;

import com.example.nursingsystem.auth.service.AuthService;
import com.example.nursingsystem.common.exception.BusinessException;
import com.example.nursingsystem.common.exception.ErrorCode;
import com.example.nursingsystem.common.utils.JwtUtil;
import com.example.nursingsystem.system.entity.Role;
import com.example.nursingsystem.system.entity.User;
import com.example.nursingsystem.system.entity.UserRole;
import com.example.nursingsystem.system.mapper.UserRoleMapper;
import com.example.nursingsystem.system.service.RoleService;
import com.example.nursingsystem.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleMapper userRoleMapper;
    private final RoleService roleService;

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
    public String getUserRole(Long userId) {
        // 查询用户的角色
        List<UserRole> userRoles = userRoleMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)
        );
        if (userRoles != null && !userRoles.isEmpty()) {
            // 获取用户的第一个角色
            UserRole userRole = userRoles.get(0);
            Role role = roleService.getById(userRole.getRoleId());
            if (role != null) {
                return role.getRoleKey(); // 返回 roleKey，通常是 admin 等标识
            }
        }
        return "user"; // 默认普通用户
    }

    @Override
    public boolean verifyToken(String token) {
        return JwtUtil.verifyToken(token);
    }

    @Override
    public void logout() {
        // TODO: 可以实现 Token 黑名单机制
    }
}
