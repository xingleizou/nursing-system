package com.example.nursingsystem.knowledge.dto;

import lombok.Data;

/**
 * 资源查询 DTO
 */
@Data
public class ResourceQueryDTO {

    /**
     * 当前页
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 标题关键词
     */
    private String title;

    /**
     * 分类 ID
     */
    private Long categoryId;

    /**
     * 标签 ID
     */
    private Long tagId;

    /**
     * 上传者 ID
     */
    private Long uploadUserId;

    /**
     * 状态 (0:待审核 1:已发布 2:已驳回 3:已下架)
     */
    private String status;

    /**
     * 排序字段 (create_time, view_count, download_count)
     */
    private String sortField = "create_time";

    /**
     * 排序方式 (asc, desc)
     */
    private String sortOrder = "desc";
}
