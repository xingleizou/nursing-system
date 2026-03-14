package com.example.nursingsystem.system.controller;

import com.example.nursingsystem.common.result.Result;
import com.example.nursingsystem.system.dto.MenuDTO;
import com.example.nursingsystem.system.entity.Menu;
import com.example.nursingsystem.system.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 */
@RestController
@RequestMapping("/api/system/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 获取菜单树形结构
     * 接口描述：获取所有菜单的树形结构，用于角色分配权限时展示复选框树
     */
    @GetMapping("/treeselect")
    public Result<Map<String, Object>> treeselect() {
        List<Menu> menus = menuService.getAllMenusTree();
        
        Map<String, Object> result = new HashMap<>();
        result.put("menus", menus);
        // 如果是编辑角色，这里可以传入已选中的菜单 ID 列表
        result.put("checkedKeys", new java.util.ArrayList<Long>());
        
        return Result.success(result);
    }

    /**
     * 获取当前用户路由菜单
     * 接口描述：根据当前用户角色，动态生成左侧导航菜单和路由表
     * TODO: 需要从登录上下文获取当前用户 ID
     */
    @GetMapping("/getRouters")
    public Result<List<Menu>> getRouters() {
        // TODO: 从登录上下文获取当前用户 ID，这里暂时用 userId=1 演示
        Long userId = 1L;
        List<Menu> menus = menuService.getMenusByUserId(userId);
        List<Menu> routers = menuService.buildRouters(menus);
        return Result.success(routers);
    }

    /**
     * 获取菜单列表（树形结构）
     */
    @GetMapping("/list")
    public Result<List<Menu>> listMenus() {
        List<Menu> menus = menuService.getAllMenusTree();
        return Result.success(menus);
    }

    /**
     * 获取菜单详情
     */
    @GetMapping("/{menuId}")
    public Result<Menu> getMenu(@PathVariable Long menuId) {
        Menu menu = this.menuService.getById(menuId);
        return Result.success(menu);
    }

    /**
     * 新增菜单
     * 接口描述：配置系统菜单（目录、菜单、按钮）
     */
    @PostMapping
    public Result<Void> createMenu(@RequestBody MenuDTO menuDTO) {
        boolean result = menuService.createMenu(menuDTO);
        return result ? Result.success() : Result.error("创建菜单失败");
    }

    /**
     * 修改菜单
     */
    @PutMapping
    public Result<Void> updateMenu(@RequestBody MenuDTO menuDTO) {
        boolean result = menuService.updateMenu(menuDTO);
        return result ? Result.success() : Result.error("更新菜单失败");
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    public Result<Void> deleteMenu(@PathVariable Long menuId) {
        boolean result = menuService.deleteMenu(menuId);
        return result ? Result.success() : Result.error("删除菜单失败");
    }
}
