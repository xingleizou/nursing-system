package com.example.nursingsystem.knowledge.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 资源上传 DTO
 */
@Data
public class ResourceUploadDTO {

    /**
     * 标题
     */
    private String title;

    /**
     * 资源简介
     */
    private String description;

    /**
     * 分类 ID
     */
    private Long categoryId;

    /**
     * 标签 ID 列表
     */
    private List<Long> tagIds;

    /**
     * 上传的文件
     */
    private MultipartFile file;

    /**
     * 是否置顶 (0:否 1:是)
     */
    private String isTop = "0";
}
