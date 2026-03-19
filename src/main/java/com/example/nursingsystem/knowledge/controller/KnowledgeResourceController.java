package com.example.nursingsystem.knowledge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nursingsystem.common.result.Result;
import com.example.nursingsystem.knowledge.dto.HotKnowledgeResourceDTO;
import com.example.nursingsystem.knowledge.dto.ResourceQueryDTO;
import com.example.nursingsystem.knowledge.dto.ResourceResponseDTO;
import com.example.nursingsystem.knowledge.dto.ResourceUploadDTO;
import com.example.nursingsystem.knowledge.dto.ZeroAccessWarningDTO;
import com.example.nursingsystem.knowledge.service.KnowledgeResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

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
    public Result<String> downloadResource(@PathVariable Long resourceId, HttpServletRequest request) {
        // TODO: 从认证信息中获取用户 ID 和用户名
        Long userId = 1L;
        String userName = "admin";

        // 获取客户端 IP
        String clientIp = getClientIp(request);

        // 调用下载服务
        String downloadUrl = knowledgeResourceService.downloadResource(resourceId, userId, userName, clientIp);

        return Result.success(downloadUrl);
    }

    @GetMapping("/preview/{resourceId}")
    public Result<String> previewResource(@PathVariable Long resourceId) {
        // 调用预览服务
        String previewUrl = knowledgeResourceService.previewResource(resourceId);
        return Result.success(previewUrl);
    }

    /**
     * 获取热门资源排行（按浏览量）
     * @param limit 返回数量限制，默认 10
     * @return 热门资源列表
     */
    @GetMapping("/top-viewed")
    public Result<List<HotKnowledgeResourceDTO>> getTopViewedResources(
            @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<HotKnowledgeResourceDTO> resources = knowledgeResourceService.getTopViewedResources(limit);
        return Result.success(resources);
    }

    /**
     * 获取热门资源排行（按下载量）
     * @param limit 返回数量限制，默认 10
     * @return 热门资源列表
     */
    @GetMapping("/top-downloaded")
    public Result<List<HotKnowledgeResourceDTO>> getTopDownloadedResources(
            @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<HotKnowledgeResourceDTO> resources = knowledgeResourceService.getTopDownloadedResources(limit);
        return Result.success(resources);
    }

    /**
     * 获取零访问预警资源列表
     * @param days 天数阈值（超过该天数未访问的资源），默认 30 天
     * @param limit 返回数量限制，默认 20
     * @return 零访问预警资源列表
     */
    @GetMapping("/zero-access-warning")
    public Result<List<ZeroAccessWarningDTO>> getZeroAccessWarnings(
            @RequestParam(value = "days", defaultValue = "30") int days,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {
        List<ZeroAccessWarningDTO> warnings = knowledgeResourceService.getZeroAccessWarnings(days, limit);
        return Result.success(warnings);
    }

    /**
     * 获取客户端 IP 地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 对于多个代理的情况，第一个IP为客户端真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }
}
