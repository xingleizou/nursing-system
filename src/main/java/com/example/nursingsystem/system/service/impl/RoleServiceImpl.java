package com.example.nursingsystem.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nursingsystem.common.exception.BusinessException;
import com.example.nursingsystem.system.dto.RoleDTO;
import com.example.nursingsystem.system.entity.Role;
import com.example.nursingsystem.system.mapper.RoleMapper;
import com.example.nursingsystem.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色服务实现类
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;

    @Override
    public Page<Role> pageRoles(RoleDTO roleDTO, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        
        // 模糊搜索角色名称
        if (StringUtils.hasText(roleDTO.getRoleName())) {
            wrapper.like(Role::getRoleName, roleDTO.getRoleName());
        }
        
        // 精确匹配角色权限字符
        if (StringUtils.hasText(roleDTO.getRoleKey())) {
            wrapper.eq(Role::getRoleKey, roleDTO.getRoleKey());
        }
        
        // 状态筛选
        if (roleDTO.getStatus() != null) {
            wrapper.eq(Role::getStatus, roleDTO.getStatus());
        }
        
        // 只查询未删除的角色
        wrapper.eq(Role::getDeleted, 0);
        
        // 按显示顺序排序
        wrapper.orderByAsc(Role::getRoleSort);
        
        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public RoleDTO getRoleDetail(Long roleId) {
        Role role = this.getById(roleId);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException("角色不存在");
        }
        
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(role.getRoleId());
        roleDTO.setRoleName(role.getRoleName());
        roleDTO.setRoleKey(role.getRoleKey());
        roleDTO.setRoleSort(role.getRoleSort());
        roleDTO.setStatus(role.getStatus());
        roleDTO.setDataScope(role.getDataScope());
        roleDTO.setRemark(role.getRemark());
        
        // 获取角色拥有的菜单 ID 列表
        List<Long> menuIds = roleMapper.selectMenuIdsByRoleId(roleId);
        roleDTO.setMenuIds(menuIds);
        
        return roleDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(RoleDTO roleDTO) {
        // 检查角色名是否已存在
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleKey, roleDTO.getRoleKey())
               .eq(Role::getDeleted, 0);
        if (this.count(wrapper) > 0) {
            throw new BusinessException("角色权限字符已存在：" + roleDTO.getRoleKey());
        }
        
        // 转换为实体
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleKey(roleDTO.getRoleKey());
        role.setRoleSort(roleDTO.getRoleSort());
        role.setStatus(roleDTO.getStatus());
        role.setDataScope(roleDTO.getDataScope());
        role.setRemark(roleDTO.getRemark());
        role.setCreateBy("admin"); // TODO: 从登录用户获取
        role.setCreateTime(LocalDateTime.now());
        
        // 保存角色
        boolean result = this.save(role);
        
        // 保存角色与菜单关联
        if (result && roleDTO.getMenuIds() != null && !roleDTO.getMenuIds().isEmpty()) {
            roleMapper.batchInsertRoleMenu(role.getRoleId(), roleDTO.getMenuIds());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(RoleDTO roleDTO) {
        Long roleId = roleDTO.getRoleId();
        if (roleId == null) {
            throw new BusinessException("角色 ID 不能为空");
        }
        
        Role oldRole = this.getById(roleId);
        if (oldRole == null || oldRole.getDeleted() == 1) {
            throw new BusinessException("角色不存在");
        }
        
        // 检查角色名是否已被其他角色使用
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleKey, roleDTO.getRoleKey())
               .ne(Role::getRoleId, roleId)
               .eq(Role::getDeleted, 0);
        if (this.count(wrapper) > 0) {
            throw new BusinessException("角色权限字符已存在：" + roleDTO.getRoleKey());
        }
        
        // 更新角色信息
        oldRole.setRoleName(roleDTO.getRoleName());
        oldRole.setRoleKey(roleDTO.getRoleKey());
        oldRole.setRoleSort(roleDTO.getRoleSort());
        oldRole.setStatus(oldRole.getStatus());
        oldRole.setDataScope(roleDTO.getDataScope());
        oldRole.setRemark(roleDTO.getRemark());
        oldRole.setUpdateTime(LocalDateTime.now());
        
        boolean result = this.updateById(oldRole);
        
        // 更新角色与菜单关联：先删除旧的，再添加新的
        if (result) {
            roleMapper.deleteRoleMenuByRoleId(roleId);
            if (roleDTO.getMenuIds() != null && !roleDTO.getMenuIds().isEmpty()) {
                roleMapper.batchInsertRoleMenu(roleId, roleDTO.getMenuIds());
            }
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoles(Long[] roleIds) {
        for (Long roleId : roleIds) {
            Role role = this.getById(roleId);
            if (role != null && role.getDeleted() == 0) {
                // 逻辑删除角色
                role.setDeleted(1);
                this.updateById(role);
                
                // 删除角色与菜单关联
                roleMapper.deleteRoleMenuByRoleId(roleId);
            }
        }
        return true;
    }

    @Override
    public List<Role> selectRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }
}
