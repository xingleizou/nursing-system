package com.example.nursingsystem.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nursingsystem.common.exception.BusinessException;
import com.example.nursingsystem.common.exception.ErrorCode;
import com.example.nursingsystem.system.dto.UserDTO;
import com.example.nursingsystem.system.entity.User;
import com.example.nursingsystem.system.entity.UserRole;
import com.example.nursingsystem.system.mapper.UserMapper;
import com.example.nursingsystem.system.mapper.UserRoleMapper;
import com.example.nursingsystem.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserRoleMapper userRoleMapper;

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return this.getOne(wrapper);
    }

    @Override
    public Page<User> pageUsers(UserDTO userDTO, Integer pageNum, Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(userDTO.getUsername())) {
            wrapper.like(User::getUsername, userDTO.getUsername());
        }
        if (StringUtils.hasText(userDTO.getPhone())) {
            wrapper.like(User::getPhone, userDTO.getPhone());
        }
        if (userDTO.getStatus() != null) {
            wrapper.eq(User::getStatus, userDTO.getStatus());
        }
        
        return this.page(page, wrapper);
    }

    @Override
    public boolean createUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        User existUser = this.getByUsername(userDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException(ErrorCode.USER_HAS_EXIST.getCode(), 
                                        ErrorCode.USER_HAS_EXIST.getMessage());
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setGender(userDTO.getGender());
        user.setStatus(userDTO.getStatus() != null ? userDTO.getStatus() : 1);
        user.setDeptId(userDTO.getDeptId());
        // 注意：不设置 roleId，因为用户和角色是多对多关系，通过中间表关联

        boolean saved = this.save(user);
        
        // 如果设置了角色 ID，保存用户角色关联
        if (saved && userDTO.getRoleId() != null) {
            // 删除旧的关联（如果有）
            userRoleMapper.deleteUserRoleByUserId(user.getUserId());
            // 插入新的关联
            userRoleMapper.batchInsertUserRole(user.getUserId(), java.util.Collections.singletonList(userDTO.getRoleId()));
        }
        
        return saved;
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        User existUser = this.getById(userDTO.getUserId());
        if (existUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST.getCode(), 
                                        ErrorCode.USER_NOT_EXIST.getMessage());
        }

        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setGender(userDTO.getGender());
        user.setStatus(userDTO.getStatus());
        user.setDeptId(userDTO.getDeptId());
        // 注意：不直接设置 roleId，需要通过中间表处理

        boolean updated = this.updateById(user);
        
        // 如果更新了角色 ID，需要同步更新用户角色关联表
        if (updated && userDTO.getRoleId() != null) {
            // 先删除旧关联
            userRoleMapper.deleteUserRoleByUserId(userDTO.getUserId());
            // 再插入新关联
            userRoleMapper.batchInsertUserRole(userDTO.getUserId(), java.util.Collections.singletonList(userDTO.getRoleId()));
        }
        
        return updated;
    }

    @Override
    public boolean deleteUser(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST.getCode(), 
                                        ErrorCode.USER_NOT_EXIST.getMessage());
        }
        return this.removeById(userId);
    }
}
