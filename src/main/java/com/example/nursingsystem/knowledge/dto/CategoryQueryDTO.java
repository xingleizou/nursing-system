package com.example.nursingsystem.knowledge.dto;

import lombok.Data;

/**
 * 分类查询DTO
 */
@Data
public class CategoryQueryDTO {

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 状态 (0:正常 1:停用)
     */
    private String status;

    /**
     * 是否系统内置 (0:否 1:是)
     */
    private String isSystem;
}