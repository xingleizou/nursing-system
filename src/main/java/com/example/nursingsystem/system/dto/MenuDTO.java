package com.example.nursingsystem.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单 DTO
 */
@Data
public class MenuDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单 ID
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单 ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 是否外链（1 是 0 否）
     */
    private Integer isFrame;

    /**
     * 菜单类型（M 目录 C 菜单 F 按钮）
     */
    private String menuType;

    /**
     * 菜单状态（0 显示 1 隐藏）
     */
    private Integer visible;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 备注
     */
    private String remark;
}
