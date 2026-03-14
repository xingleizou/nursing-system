package com.example.nursingsystem.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色 DTO
 */
@Data
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 显示顺序
     */
    private Integer roleSort;

    /**
     * 角色状态（0 正常 1 停用）
     */
    private Integer status;

    /**
     * 数据范围（1：全部数据权限 2：自定义数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限）
     */
    private String dataScope;

    /**
     * 备注
     */
    private String remark;

    /**
     * 菜单 ID 列表
     */
    private List<Long> menuIds;
}
