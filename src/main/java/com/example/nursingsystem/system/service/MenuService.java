package com.example.nursingsystem.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.nursingsystem.system.dto.MenuDTO;
import com.example.nursingsystem.system.entity.Menu;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取所有菜单树形结构
     * @return 菜单树
     */
    List<Menu> getAllMenusTree();

    /**
     * 根据角色 ID 获取菜单树形结构
     * @param roleId 角色 ID
     * @return 菜单树
     */
    List<Menu> getMenusTreeByRoleId(Long roleId);

    /**
     * 根据用户 ID 获取菜单权限
     * @param userId 用户 ID
     * @return 菜单列表
     */
    List<Menu> getMenusByUserId(Long userId);

    /**
     * 构建前端路由菜单
     * @param menus 菜单列表
     * @return 路由菜单列表
     */
    List<Menu> buildRouters(List<Menu> menus);

    /**
     * 新增菜单
     * @param menuDTO 菜单信息
     * @return 是否成功
     */
    boolean createMenu(MenuDTO menuDTO);

    /**
     * 修改菜单
     * @param menuDTO 菜单信息
     * @return 是否成功
     */
    boolean updateMenu(MenuDTO menuDTO);

    /**
     * 删除菜单
     * @param menuId 菜单 ID
     * @return 是否成功
     */
    boolean deleteMenu(Long menuId);

    /**
     * 将菜单列表转换为树形结构
     * @param menus 菜单列表
     * @param rootId 根节点 ID（通常为 0）
     * @return 树形菜单列表
     */
    List<Menu> buildMenuTree(List<Menu> menus, Long rootId);
}
