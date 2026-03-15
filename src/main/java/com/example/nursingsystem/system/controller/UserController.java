package com.example.nursingsystem.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nursingsystem.common.result.Result;
import com.example.nursingsystem.system.dto.UserDTO;
import com.example.nursingsystem.system.entity.User;
import com.example.nursingsystem.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据 ID 获取用户
     */
    @GetMapping("/{userId}")
    public Result<User> getUser(@PathVariable Long userId) {
        User user = userService.getById(userId);
        return Result.success(user);
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public Result<Page<Map<String, Object>>> pageUsers(UserDTO userDTO,
                                        @RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Map<String, Object>> page = userService.pageUsers(userDTO, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result<Void> createUser(@RequestBody UserDTO userDTO) {
        boolean result = userService.createUser(userDTO);
        return result ? Result.success() : Result.error("创建失败");
    }

    /**
     * 更新用户
     */
    @PutMapping
    public Result<Void> updateUser(@RequestBody UserDTO userDTO) {
        boolean result = userService.updateUser(userDTO);
        return result ? Result.success() : Result.error("更新失败");
    }

    /**
     * 删除用户 (逻辑删除)
     */
    @DeleteMapping("/{userId}")
    public Result<Void> deleteUser(@PathVariable Long userId) {
        boolean result = userService.deleteUser(userId);
        return result ? Result.success() : Result.error("删除失败");
    }

    /**
     * 物理删除用户 (彻底删除，不可恢复)
     * 注意：此操作仅用于 GDPR 合规或特殊场景，需谨慎使用！
     */
    @DeleteMapping("/{userId}/physical")
    public Result<Void> physicallyDeleteUser(@PathVariable Long userId) {
        boolean result = userService.physicallyDeleteUser(userId);
        return result ? Result.success() : Result.error("物理删除失败");
    }
}
