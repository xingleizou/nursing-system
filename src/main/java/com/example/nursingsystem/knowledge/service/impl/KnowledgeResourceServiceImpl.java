package com.example.nursingsystem.knowledge.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nursingsystem.common.exception.BusinessException;
import com.example.nursingsystem.common.utils.AliOSSUtil;
import com.example.nursingsystem.knowledge.dto.ResourceQueryDTO;
import com.example.nursingsystem.knowledge.dto.ResourceResponseDTO;
import com.example.nursingsystem.knowledge.dto.ResourceUploadDTO;
import com.example.nursingsystem.knowledge.entity.*;
import com.example.nursingsystem.knowledge.mapper.*;
import com.example.nursingsystem.knowledge.service.KnowledgeResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 知识资源服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeResourceServiceImpl extends ServiceImpl<KnowledgeResourceMapper, KnowledgeResource> implements KnowledgeResourceService {

    private final KnowledgeResourceMapper knowledgeResourceMapper;
    private final KnowledgeCategoryMapper knowledgeCategoryMapper;
    private final KnowledgeTagMapper knowledgeTagMapper;
    private final KnowledgeResourceTagMapper knowledgeResourceTagMapper;
    private final KnowledgeAuditLogMapper knowledgeAuditLogMapper;
    private final AliOSSUtil aliOSSUtil;

    // 最大文件大小 (100MB)
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long uploadResource(ResourceUploadDTO uploadDTO, Long userId, String userName) {
        MultipartFile file = uploadDTO.getFile();
        
        // 1. 文件校验
        validateFile(file);

        // 2. 计算文件 MD5
        String md5Code = calculateMd5(file);

        // 3. MD5 去重检查
        KnowledgeResource existingResource = knowledgeResourceMapper.selectByMd5(md5Code);
        if (existingResource != null) {
            throw new BusinessException("该文件已存在，资源 ID: " + existingResource.getResourceId());
        }

        // 4. 保存文件到本地或 OSS
        String filePath = saveFile(file);

        // 5. 创建资源记录
        KnowledgeResource resource = new KnowledgeResource();
        resource.setTitle(uploadDTO.getTitle());
        resource.setDescription(uploadDTO.getDescription());
        resource.setCategoryId(uploadDTO.getCategoryId());
        resource.setFileName(file.getOriginalFilename());
        resource.setFilePath(filePath);
        resource.setFileSize(file.getSize());
        resource.setFileType(file.getContentType());
        resource.setFileExtension(FileUtil.getSuffix(file.getOriginalFilename()));
        resource.setMd5Code(md5Code);
        resource.setViewCount(0);
        resource.setDownloadCount(0);
        resource.setStatus("0"); // 待审核状态
        resource.setIsTop(uploadDTO.getIsTop());
        resource.setUploadUserId(userId);
        resource.setUploadUserName(userName);
        
        knowledgeResourceMapper.insert(resource);

        // 6. 保存标签关联
        if (uploadDTO.getTagIds() != null && !uploadDTO.getTagIds().isEmpty()) {
            for (Long tagId : uploadDTO.getTagIds()) {
                KnowledgeResourceTag resourceTag = new KnowledgeResourceTag();
                resourceTag.setResourceId(resource.getResourceId());
                resourceTag.setTagId(tagId);
                knowledgeResourceTagMapper.insert(resourceTag);
            }
        }

        log.info("资源上传成功，资源 ID: {}, 文件名：{}", resource.getResourceId(), file.getOriginalFilename());
        return resource.getResourceId();
    }

    @Override
    public Page<ResourceResponseDTO> pageResources(ResourceQueryDTO queryDTO) {
        Page<KnowledgeResource> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        LambdaQueryWrapper<KnowledgeResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeResource::getDeleted, 0);
        
        // 查询条件
        if (StrUtil.isNotBlank(queryDTO.getTitle())) {
            wrapper.like(KnowledgeResource::getTitle, queryDTO.getTitle());
        }
        if (queryDTO.getCategoryId() != null) {
            wrapper.eq(KnowledgeResource::getCategoryId, queryDTO.getCategoryId());
        }
        if (queryDTO.getUploadUserId() != null) {
            wrapper.eq(KnowledgeResource::getUploadUserId, queryDTO.getUploadUserId());
        }
        if (StrUtil.isNotBlank(queryDTO.getStatus())) {
            wrapper.eq(KnowledgeResource::getStatus, queryDTO.getStatus());
        }
        
        // 排序
        if ("view_count".equals(queryDTO.getSortField())) {
            wrapper.orderByDesc(KnowledgeResource::getViewCount);
        } else if ("download_count".equals(queryDTO.getSortField())) {
            wrapper.orderByDesc(KnowledgeResource::getDownloadCount);
        } else {
            wrapper.orderByDesc(KnowledgeResource::getCreateTime);
        }

        Page<KnowledgeResource> resultPage = knowledgeResourceMapper.selectPage(page, wrapper);

        // 转换为响应 DTO
        List<ResourceResponseDTO> responseList = resultPage.getRecords().stream()
                .map(this::convertToResponseDTO)
                .toList();

        Page<ResourceResponseDTO> responsePage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        responsePage.setRecords(responseList);
        
        return responsePage;
    }

    @Override
    public ResourceResponseDTO getResourceById(Long resourceId) {
        KnowledgeResource resource = knowledgeResourceMapper.selectById(resourceId);
        if (resource == null || resource.getDeleted() == 1) {
            throw new BusinessException("资源不存在");
        }
        
        ResourceResponseDTO responseDTO = convertToResponseDTO(resource);
        
        // 增加浏览次数
        incrementViewCount(resourceId);
        
        return responseDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementDownloadCount(Long resourceId) {
        knowledgeResourceMapper.incrementDownloadCount(resourceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long resourceId) {
        knowledgeResourceMapper.incrementViewCount(resourceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditResource(Long resourceId, String auditAction, String auditRemark, 
                              Long auditorId, String auditorName) {
        KnowledgeResource resource = knowledgeResourceMapper.selectById(resourceId);
        if (resource == null || resource.getDeleted() == 1) {
            throw new BusinessException("资源不存在");
        }

        // 更新资源状态
        if ("1".equals(auditAction)) {
            resource.setStatus("1"); // 已发布
        } else if ("2".equals(auditAction)) {
            resource.setStatus("2"); // 已驳回
        }
        resource.setAuditRemark(auditRemark);
        knowledgeResourceMapper.updateById(resource);

        // 记录审核日志
        KnowledgeAuditLog auditLog = new KnowledgeAuditLog();
        auditLog.setResourceId(resourceId);
        auditLog.setAuditAction(auditAction);
        auditLog.setAuditRemark(auditRemark);
        auditLog.setAuditorId(auditorId);
        auditLog.setAuditorName(auditorName);
        auditLog.setAuditTime(LocalDateTime.now());
        auditLog.setCreateTime(LocalDateTime.now());
        knowledgeAuditLogMapper.insert(auditLog);

        log.info("资源审核完成，资源 ID: {}, 审核结果：{}, 审核人：{}", resourceId, auditAction, auditorName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteResource(Long resourceId) {
        KnowledgeResource resource = knowledgeResourceMapper.selectById(resourceId);
        if (resource == null || resource.getDeleted() == 1) {
            throw new BusinessException("资源不存在");
        }

        // 逻辑删除
        resource.setDeleted(1);
        knowledgeResourceMapper.updateById(resource);

        log.info("资源已删除，资源 ID: {}", resourceId);
    }

    /**
     * 文件校验
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("文件大小不能超过 100MB");
        }

        // 检查文件扩展名
        String extension = FileUtil.getSuffix(file.getOriginalFilename()).toLowerCase();
        List<String> allowedExtensions = List.of("pdf", "doc", "docx", "xls", "xlsx", 
                                                  "ppt", "pptx", "mp4", "avi", "mov", 
                                                  "wmv", "jpg", "jpeg", "png", "gif");
        if (!allowedExtensions.contains(extension)) {
            throw new BusinessException("不支持的文件类型：" + extension);
        }
    }

    /**
     * 计算文件 MD5
     */
    private String calculateMd5(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, bytesRead);
            }
            
            byte[] digestBytes = md5.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digestBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("计算 MD5 失败", e);
            throw new BusinessException("文件处理失败");
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 算法不可用", e);
            throw new BusinessException("系统错误");
        }
    }

    /**
     * 保存文件到阿里云 OSS
     */
    private String saveFile(MultipartFile file) {
        try {
            // 使用阿里云 OSS 上传文件
            String fileUrl = aliOSSUtil.upload(file);
            
            log.info("文件上传 OSS 成功，URL: {}", fileUrl);
            return fileUrl;
        } catch (IOException e) {
            log.error("OSS 文件上传失败", e);
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 转换为响应 DTO
     */
    private ResourceResponseDTO convertToResponseDTO(KnowledgeResource resource) {
        ResourceResponseDTO responseDTO = new ResourceResponseDTO();
        BeanUtils.copyProperties(resource, responseDTO);
        
        // 格式化文件大小
        responseDTO.setFileSizeFormatted(formatFileSize(resource.getFileSize()));
        
        // 状态描述
        responseDTO.setStatusDesc(getStatusDesc(resource.getStatus()));
        
        // 获取分类名称
        if (resource.getCategoryId() != null) {
            KnowledgeCategory category = knowledgeCategoryMapper.selectById(resource.getCategoryId());
            if (category != null) {
                responseDTO.setCategoryName(category.getCategoryName());
            }
        }
        
        // 获取标签列表
        if (resource.getResourceId() != null) {
            LambdaQueryWrapper<KnowledgeResourceTag> tagWrapper = new LambdaQueryWrapper<>();
            tagWrapper.eq(KnowledgeResourceTag::getResourceId, resource.getResourceId());
            List<KnowledgeResourceTag> resourceTags = knowledgeResourceTagMapper.selectList(tagWrapper);
            
            if (resourceTags != null && !resourceTags.isEmpty()) {
                List<ResourceResponseDTO.TagDTO> tagDTOs = new ArrayList<>();
                for (KnowledgeResourceTag rt : resourceTags) {
                    KnowledgeTag tag = knowledgeTagMapper.selectById(rt.getTagId());
                    if (tag != null) {
                        ResourceResponseDTO.TagDTO tagDTO = new ResourceResponseDTO.TagDTO();
                        tagDTO.setTagId(tag.getTagId());
                        tagDTO.setTagName(tag.getTagName());
                        tagDTO.setColor(tag.getColor());
                        tagDTOs.add(tagDTO);
                    }
                }
                responseDTO.setTags(tagDTOs);
            }
        }
        
        // 格式化时间
        if (resource.getCreateTime() != null) {
            responseDTO.setCreateTime(DateUtil.formatDateTime(java.util.Date.from(resource.getCreateTime().atZone(java.time.ZoneId.systemDefault()).toInstant())));
        }
        if (resource.getUpdateTime() != null) {
            responseDTO.setUpdateTime(DateUtil.formatDateTime(java.util.Date.from(resource.getUpdateTime().atZone(java.time.ZoneId.systemDefault()).toInstant())));
        }
        
        return responseDTO;
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(Long size) {
        if (size == null) return "0 B";
        
        String[] units = {"B", "KB", "MB", "GB"};
        int i = 0;
        double s = size;
        
        while (s >= 1024 && i < units.length - 1) {
            s /= 1024;
            i++;
        }
        
        return String.format("%.2f %s", s, units[i]);
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(String status) {
        switch (status) {
            case "0": return "待审核";
            case "1": return "已发布";
            case "2": return "已驳回";
            case "3": return "已下架";
            default: return "未知";
        }
    }
}
