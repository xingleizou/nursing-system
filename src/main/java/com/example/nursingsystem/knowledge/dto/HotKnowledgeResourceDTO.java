package com.example.nursingsystem.knowledge.dto;

import lombok.Data;

/**
 * 热门知识资源 DTO
 */
@Data
public class HotKnowledgeResourceDTO {

    /**
     * 资源 ID
     */
    private Long resourceId;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 文件类型（PDF/Word/PPT/MP4 等）
     */
    private String fileType;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 上传者姓名
     */
    private String uploadUserName;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 上传时间
     */
    private String createTime;

    /**
     * 排名
     */
    private Integer rank;
}
