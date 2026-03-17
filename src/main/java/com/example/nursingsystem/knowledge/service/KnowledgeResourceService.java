package com.example.nursingsystem.knowledge.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nursingsystem.knowledge.dto.ResourceQueryDTO;
import com.example.nursingsystem.knowledge.dto.ResourceResponseDTO;
import com.example.nursingsystem.knowledge.dto.ResourceUploadDTO;
import com.example.nursingsystem.knowledge.entity.KnowledgeResource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 知识资源服务接口
 */
public interface KnowledgeResourceService extends IService<KnowledgeResource> {

    /**
     * 上传资源
     * @param uploadDTO 上传 DTO
     * @param userId 当前用户 ID
     * @param userName 当前用户名
     * @return 资源 ID
     */
    Long uploadResource(ResourceUploadDTO uploadDTO, Long userId, String userName);

    /**
     * 分页查询资源
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<ResourceResponseDTO> pageResources(ResourceQueryDTO queryDTO);

    /**
     * 根据 ID 获取资源详情
     * @param resourceId 资源 ID
     * @return 资源详情
     */
    ResourceResponseDTO getResourceById(Long resourceId);

    /**
     * 增加下载次数
     * @param resourceId 资源 ID
     */
    void incrementDownloadCount(Long resourceId);

    /**
     * 增加浏览次数
     * @param resourceId 资源 ID
     */
    void incrementViewCount(Long resourceId);

    /**
     * 审核资源
     * @param resourceId 资源 ID
     * @param auditAction 审核操作 (1:通过 2:驳回)
     * @param auditRemark 审核意见
     * @param auditorId 审核人 ID
     * @param auditorName 审核人姓名
     */
    void auditResource(Long resourceId, String auditAction, String auditRemark, Long auditorId, String auditorName);

    /**
     * 删除资源 (逻辑删除)
     * @param resourceId 资源 ID
     */
    void deleteResource(Long resourceId);
}