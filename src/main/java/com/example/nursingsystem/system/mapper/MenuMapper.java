package com.example.nursingsystem.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nursingsystem.system.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单 Mapper 接口
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 查询所有菜单（树形结构）
     * @return 菜单列表
     */
    List<Menu> selectAllMenus();

    /**
     * 根据角色 ID 查询菜单权限
     * @param roleId 角色 ID
     * @return 菜单列表
     */
    List<Menu> selectMenusByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户 ID 查询菜单权限
     * @param userId 用户 ID
     * @return 菜单列表
     */
    List<Menu> selectMenusByUserId(@Param("userId") Long userId);

    /**
     * 查询菜单 ID 列表
     * @param roleId 角色 ID
     * @return 菜单 ID 列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
