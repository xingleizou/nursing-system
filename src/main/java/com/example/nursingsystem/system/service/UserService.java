package com.example.nursingsystem.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nursingsystem.system.dto.UserDTO;
import com.example.nursingsystem.system.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);

    /**
     * 分页查询用户
     */
    Page<Map<String, Object>> pageUsers(UserDTO userDTO, Integer pageNum, Integer pageSize);

    /**
     * 创建用户
     */
    boolean createUser(UserDTO userDTO);

    /**
     * 更新用户
     */
    boolean updateUser(UserDTO userDTO);

    /**
     * 删除用户
     */
    boolean deleteUser(Long userId);

    /**
     * 物理删除用户 (彻底删除，不可恢复)
     * @param userId 用户 ID
     * @return 是否成功
     */
    boolean physicallyDeleteUser(Long userId);
}
