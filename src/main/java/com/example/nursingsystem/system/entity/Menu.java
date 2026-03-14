package com.example.nursingsystem.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统菜单实体类
 */
@Data
@TableName("sys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单 ID
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 路由名称 (用于前端路由，非数据库字段)
     */
    @TableField(exist = false)
    private String name;

    /**
     * 路由元信息 (用于前端路由，非数据库字段)
     */
    @TableField(exist = false)
    private Object meta;

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

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志（0 代表存在 1 代表删除）
     */
    @TableLogic
    private Integer deleted;

    /**
     * 子菜单列表
     */
    @TableField(exist = false)
    private List<Menu> children;
}
