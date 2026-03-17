package com.example.nursingsystem.auth.controller;

import com.example.nursingsystem.auth.dto.LoginDTO;
import com.example.nursingsystem.auth.service.AuthService;
import com.example.nursingsystem.common.result.Result;
import com.example.nursingsystem.system.entity.User;
import com.example.nursingsystem.system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
            
        // 查询用户信息
        User user = userService.getByUsername(loginDTO.getUsername());
        if (user != null) {
            // 获取用户角色
            String roleCode = authService.getUserRole(user.getUserId());
                
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("tokenPrefix", "Bearer ");
            result.put("userId", user.getUserId());
            result.put("username", user.getUsername());
            result.put("nickname", user.getNickname());
            result.put("roleCode", roleCode); // 添加角色代码
            log.info("用户登录成功：{}, 角色：{}", loginDTO.getUsername(), roleCode);
            log.info("JWT 令牌生成成功：{}", token);
            return Result.success(result);
        }
            
        return Result.error("用户不存在");
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.success();
    }

    /**
     * 验证 Token
     */
    @GetMapping("/verify")
    public Result<Boolean> verifyToken(@RequestParam String token) {
        boolean valid = authService.verifyToken(token);
        return Result.success(valid);
    }
}
