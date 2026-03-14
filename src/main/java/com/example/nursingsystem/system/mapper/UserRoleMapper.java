package com.example.nursingsystem.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nursingsystem.system.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色关联 Mapper 接口
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户 ID 删除角色关联
     * @param userId 用户 ID
     * @return 影响行数
     */
    int deleteUserRoleByUserId(@Param("userId") Long userId);

    /**
     * 批量插入用户角色关联
     * @param userId 用户 ID
     * @param roleIds 角色 ID 列表
     * @return 影响行数
     */
    int batchInsertUserRole(@Param("userId") Long userId, @Param("roleIds") java.util.List<Long> roleIds);
}
