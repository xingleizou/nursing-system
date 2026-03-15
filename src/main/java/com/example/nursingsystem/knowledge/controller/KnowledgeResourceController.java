package com.example.nursingsystem.knowledge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nursingsystem.common.result.Result;
import com.example.nursingsystem.knowledge.dto.ResourceQueryDTO;
import com.example.nursingsystem.knowledge.dto.ResourceResponseDTO;
import com.example.nursingsystem.knowledge.dto.ResourceUploadDTO;
import com.example.nursingsystem.knowledge.service.KnowledgeResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 知识资源控制器
 */
@RestController
@RequestMapping("/api/knowledge/resource")
@RequiredArgsConstructor
public class KnowledgeResourceController {

    private final KnowledgeResourceService knowledgeResourceService;

    @PostMapping("/upload")
    public Result<Long> uploadResource(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "tagIds", required = false) java.util.List<Long> tagIds,
            @RequestParam(value = "isTop", defaultValue = "0") String isTop,
            @RequestParam("file") MultipartFile file) {
        
        // 构建上传 DTO
        ResourceUploadDTO uploadDTO = new ResourceUploadDTO();
        uploadDTO.setTitle(title);
        uploadDTO.setDescription(description);
        uploadDTO.setCategoryId(categoryId);
        uploadDTO.setTagIds(tagIds);
        uploadDTO.setIsTop(isTop);
        uploadDTO.setFile(file);

        // TODO: 从认证信息中获取用户 ID 和用户名
        Long userId = 1L;
        String userName = "admin";

        Long resourceId = knowledgeResourceService.uploadResource(uploadDTO, userId, userName);
        return Result.success(resourceId);
    }

    @GetMapping("/page")
    public Result<Page<ResourceResponseDTO>> pageResources(ResourceQueryDTO queryDTO) {
        Page<ResourceResponseDTO> page = knowledgeResourceService.pageResources(queryDTO);
        return Result.success(page);
    }

    @GetMapping("/{resourceId}")
    public Result<ResourceResponseDTO> getResource(@PathVariable Long resourceId) {
        ResourceResponseDTO resource = knowledgeResourceService.getResourceById(resourceId);
        return Result.success(resource);
    }

    @PostMapping("/audit/{resourceId}")
    public Result<Void> auditResource(
            @PathVariable Long resourceId,
            @RequestParam("auditAction") String auditAction,
            @RequestParam(value = "auditRemark", required = false) String auditRemark) {
        
        // TODO: 从认证信息中获取审核人信息
        Long auditorId = 1L;
        String auditorName = "admin";

        knowledgeResourceService.auditResource(resourceId, auditAction, auditRemark, auditorId, auditorName);
        return Result.success();
    }

    @DeleteMapping("/{resourceId}")
    public Result<Void> deleteResource(@PathVariable Long resourceId) {
        knowledgeResourceService.deleteResource(resourceId);
        return Result.success();
    }

    @PostMapping("/download/{resourceId}")
    public Result<String> downloadResource(@PathVariable Long resourceId) {
        // TODO: 实现下载功能（包含权限校验和日志记录）
        ResourceResponseDTO resource = knowledgeResourceService.getResourceById(resourceId);
        
        // 增加下载次数
        knowledgeResourceService.incrementDownloadCount(resourceId);
        
        // TODO: 记录下载日志
        // TODO: 返回文件下载 URL 或流式输出
        
        return Result.success(resource.getFilePath());
    }
}
