package com.example.nursingsystem.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.nursingsystem.common.annotation.DataScope;
import com.example.nursingsystem.system.dto.RoleDTO;
import com.example.nursingsystem.system.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService extends IService<Role> {

    /**
     * 分页查询角色列表
     * @param roleDTO 角色查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 角色分页数据
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    Page<Role> pageRoles(RoleDTO roleDTO, Integer pageNum, Integer pageSize);

    /**
     * 获取角色详情及菜单权限
     * @param roleId 角色 ID
     * @return 角色信息
     */
    RoleDTO getRoleDetail(Long roleId);

    /**
     * 新增角色
     * @param roleDTO 角色信息
     * @return 是否成功
     */
    boolean createRole(RoleDTO roleDTO);

    /**
     * 修改角色
     * @param roleDTO 角色信息
     * @return 是否成功
     */
    boolean updateRole(RoleDTO roleDTO);

    /**
     * 删除角色
     * @param roleIds 角色 ID 数组
     * @return 是否成功
     */
    boolean deleteRoles(Long[] roleIds);

    /**
     * 根据用户 ID 查询角色列表
     * @param userId 用户 ID
     * @return 角色列表
     */
    List<Role> selectRolesByUserId(Long userId);
}
