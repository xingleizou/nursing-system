package com.example.nursingsystem.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nursingsystem.common.result.Result;
import com.example.nursingsystem.system.dto.RoleDTO;
import com.example.nursingsystem.system.entity.Role;
import com.example.nursingsystem.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/api/system/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 分页查询角色列表
     * 接口描述：获取角色列表，支持按角色名称、权限字符筛选
     * 请求参数：roleName(角色名称模糊搜索), roleKey(权限字符), status(状态)
     */
    @GetMapping("/page")
    public Result<Page<Role>> listRoles(RoleDTO roleDTO,
                                        @RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Role> page = roleService.pageRoles(roleDTO, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 获取所有角色列表（用于下拉选择）
     * 接口描述：获取所有正常状态的角色，用于用户管理等场景的下拉选择
     */
    @GetMapping("/list")
    public Result<List<Role>> listAllRoles() {
        List<Role> roles = roleService.list();
        return Result.success(roles);
    }

    /**
     * 获取角色详情
     * 接口描述：获取角色详情及拥有的菜单权限
     */
    @GetMapping("/{roleId}")
    public Result<RoleDTO> getRole(@PathVariable Long roleId) {
        RoleDTO roleDTO = roleService.getRoleDetail(roleId);
        return Result.success(roleDTO);
    }

    /**
     * 新增角色
     * 接口描述：创建新角色并分配菜单权限
     */
    @PostMapping
    public Result<Void> createRole(@RequestBody RoleDTO roleDTO) {
        boolean result = roleService.createRole(roleDTO);
        return result ? Result.success() : Result.error("创建角色失败");
    }

    /**
     * 修改角色
     * 接口描述：更新角色信息及权限
     */
    @PutMapping
    public Result<Void> updateRole(@RequestBody RoleDTO roleDTO) {
        boolean result = roleService.updateRole(roleDTO);
        return result ? Result.success() : Result.error("更新角色失败");
    }

    /**
     * 删除角色
     * 接口描述：删除角色（支持批量删除）
     */
    @DeleteMapping("/{roleIds}")
    public Result<Void> deleteRoles(@PathVariable Long[] roleIds) {
        boolean result = roleService.deleteRoles(roleIds);
        return result ? Result.success() : Result.error("删除角色失败");
    }

    /**
     * 根据用户 ID 查询角色列表
     * 接口描述：获取用户拥有的所有角色
     */
    @GetMapping("/user/{userId}")
    public Result<List<Role>> listRolesByUser(@PathVariable Long userId) {
        List<Role> roles = roleService.selectRolesByUserId(userId);
        return Result.success(roles);
    }
}
