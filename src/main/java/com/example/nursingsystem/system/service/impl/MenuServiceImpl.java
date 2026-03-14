package com.example.nursingsystem.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nursingsystem.common.exception.BusinessException;
import com.example.nursingsystem.system.dto.MenuDTO;
import com.example.nursingsystem.system.entity.Menu;
import com.example.nursingsystem.system.mapper.MenuMapper;
import com.example.nursingsystem.system.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final MenuMapper menuMapper;

    @Override
    public List<Menu> getAllMenusTree() {
        List<Menu> allMenus = menuMapper.selectAllMenus();
        return buildMenuTree(allMenus, 0L);
    }

    @Override
    public List<Menu> getMenusTreeByRoleId(Long roleId) {
        List<Menu> menus = menuMapper.selectMenusByRoleId(roleId);
        return buildMenuTree(menus, 0L);
    }

    @Override
    public List<Menu> getMenusByUserId(Long userId) {
        return menuMapper.selectMenusByUserId(userId);
    }

    @Override
    public List<Menu> buildRouters(List<Menu> menus) {
        List<Menu> routerList = new ArrayList<>();
        for (Menu menu : menus) {
            if ("M".equals(menu.getMenuType())) {
                // 目录类型
                Menu router = new Menu();
                router.setPath(menu.getPath());
                router.setComponent("Layout");
                router.setName(capitalizeFirstLetter(menu.getPath())); // 设置路由名称
                router.setMeta(menu); // 设置路由元信息
                
                // 查找子菜单
                List<Menu> children = menus.stream()
                    .filter(m -> m.getParentId().equals(menu.getMenuId()))
                    .collect(Collectors.toList());
                
                if (!children.isEmpty()) {
                    router.setChildren(buildRouters(children));
                }
                
                routerList.add(router);
            } else if ("C".equals(menu.getMenuType())) {
                // 菜单类型
                Menu router = new Menu();
                router.setPath(menu.getPath());
                router.setComponent(menu.getComponent());
                router.setName(capitalizeFirstLetter(menu.getPath())); // 设置路由名称
                router.setMeta(menu); // 设置路由元信息
                routerList.add(router);
            }
            // F 按钮类型不生成路由
        }
        return routerList;
    }

    @Override
    public boolean createMenu(MenuDTO menuDTO) {
        // 检查菜单名是否已存在
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getMenuName, menuDTO.getMenuName())
               .eq(Menu::getParentId, menuDTO.getParentId())
               .eq(Menu::getDeleted, 0);
        if (this.count(wrapper) > 0) {
            throw new BusinessException("菜单名称已存在");
        }
        
        Menu menu = new Menu();
        menu.setMenuName(menuDTO.getMenuName());
        menu.setParentId(menuDTO.getParentId());
        menu.setOrderNum(menuDTO.getOrderNum());
        menu.setPath(menuDTO.getPath());
        menu.setComponent(menuDTO.getComponent());
        menu.setIsFrame(menuDTO.getIsFrame());
        menu.setMenuType(menuDTO.getMenuType());
        menu.setVisible(menuDTO.getVisible());
        menu.setPerms(menuDTO.getPerms());
        menu.setIcon(menuDTO.getIcon());
        menu.setRemark(menuDTO.getRemark());
        menu.setCreateBy("admin"); // TODO: 从登录用户获取
        menu.setCreateTime(LocalDateTime.now());
        
        return this.save(menu);
    }

    @Override
    public boolean updateMenu(MenuDTO menuDTO) {
        Long menuId = menuDTO.getMenuId();
        if (menuId == null) {
            throw new BusinessException("菜单 ID 不能为空");
        }
        
        Menu oldMenu = this.getById(menuId);
        if (oldMenu == null || oldMenu.getDeleted() == 1) {
            throw new BusinessException("菜单不存在");
        }
        
        // 检查菜单名是否已被其他菜单使用
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getMenuName, menuDTO.getMenuName())
               .eq(Menu::getParentId, menuDTO.getParentId())
               .ne(Menu::getMenuId, menuId)
               .eq(Menu::getDeleted, 0);
        if (this.count(wrapper) > 0) {
            throw new BusinessException("菜单名称已存在");
        }
        
        oldMenu.setMenuName(menuDTO.getMenuName());
        oldMenu.setParentId(menuDTO.getParentId());
        oldMenu.setOrderNum(menuDTO.getOrderNum());
        oldMenu.setPath(menuDTO.getPath());
        oldMenu.setComponent(menuDTO.getComponent());
        oldMenu.setIsFrame(menuDTO.getIsFrame());
        oldMenu.setMenuType(menuDTO.getMenuType());
        oldMenu.setVisible(menuDTO.getVisible());
        oldMenu.setPerms(menuDTO.getPerms());
        oldMenu.setIcon(menuDTO.getIcon());
        oldMenu.setRemark(menuDTO.getRemark());
        oldMenu.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(oldMenu);
    }

    @Override
    public boolean deleteMenu(Long menuId) {
        Menu menu = this.getById(menuId);
        if (menu == null || menu.getDeleted() == 1) {
            throw new BusinessException("菜单不存在");
        }
        
        // 检查是否有子菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, menuId)
               .eq(Menu::getDeleted, 0);
        if (this.count(wrapper) > 0) {
            throw new BusinessException("存在子菜单，无法删除");
        }
        
        // 逻辑删除
        menu.setDeleted(1);
        return this.updateById(menu);
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus, Long rootId) {
        return menus.stream()
            .filter(menu -> menu.getParentId().equals(rootId))
            .peek(menu -> menu.setChildren(buildMenuTree(menus, menu.getMenuId())))
            .collect(Collectors.toList());
    }

    /**
     * 首字母大写
     */
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
