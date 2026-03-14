package com.example.nursingsystem.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nursingsystem.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 Mapper 接口
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户 ID 查询角色
     * @param userId 用户 ID
     * @return 角色列表
     */
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 删除角色与菜单关联
     * @param roleId 角色 ID
     * @return 影响行数
     */
    int deleteRoleMenuByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量新增角色菜单关联
     * @param roleId 角色 ID
     * @param menuIds 菜单 ID 列表
     * @return 影响行数
     */
    int batchInsertRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    /**
     * 查询角色的菜单 ID 列表
     * @param roleId 角色 ID
     * @return 菜单 ID 列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
