package com.example.nursingsystem.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 知识分类实体类
 */
@Data
@TableName("knowledge_category")
public class KnowledgeCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类 ID
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    /**
     * 父分类 ID (0:顶级分类)
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 是否系统内置 (0:否 1:是，不可删除)
     */
    private String isSystem;

    /**
     * 状态 (0:正常 1:停用)
     */
    private String status;

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
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志 (0:存在 1:删除)
     */
    @TableLogic
    private Integer deleted;
}
