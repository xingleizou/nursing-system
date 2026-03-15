package com.example.nursingsystem.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nursingsystem.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User selectUserByUsername(@Param("username") String username);

    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户信息
     */
    User selectUserByPhone(@Param("phone") String phone);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    User selectUserByEmail(@Param("email") String email);

    /**
     * 根据用户 ID 查询用户详情（包含角色信息）
     * @param userId 用户 ID
     * @return 用户信息
     */
    User selectUserDetailById(@Param("userId") Long userId);

    /**
     * 根据用户 ID 查询角色 ID 列表
     * @param userId 用户 ID
     * @return 角色 ID 列表
     */
    java.util.List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 批量删除用户 (逻辑删除)
     * @param userIds 用户 ID 数组
     * @return 影响行数
     */
    int batchDeleteUsers(@Param("userIds") Long[] userIds);

    /**
     * 物理删除用户 (真正删除记录)
     * @param userId 用户 ID
     * @return 影响行数
     */
    int deleteByIdPhysical(@Param("userId") Long userId);
}
