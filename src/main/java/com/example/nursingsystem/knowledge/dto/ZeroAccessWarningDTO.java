package com.example.nursingsystem.knowledge.dto;

import lombok.Data;

/**
 * 零访问知识资源预警 DTO
 */
@Data
public class ZeroAccessWarningDTO {

    /**
     * 资源 ID
     */
    private Long resourceId;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 文件类型
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
     * 最后访问时间（如果有）
     */
    private String lastAccessTime;

    /**
     * 未访问天数
     */
    private Long daysWithoutAccess;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;
}
