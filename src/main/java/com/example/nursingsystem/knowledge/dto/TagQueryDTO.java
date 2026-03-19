package com.example.nursingsystem.knowledge.dto;

import lombok.Data;

/**
 * 标签查询DTO
 */
@Data
public class TagQueryDTO {

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 状态 (0:正常 1:停用)
     */
    private String status;
}