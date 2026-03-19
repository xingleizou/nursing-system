package com.example.nursingsystem.knowledge.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nursingsystem.knowledge.dto.HotKnowledgeResourceDTO;
import com.example.nursingsystem.knowledge.dto.ResourceQueryDTO;
import com.example.nursingsystem.knowledge.dto.ResourceResponseDTO;
import com.example.nursingsystem.knowledge.dto.ResourceUploadDTO;
import com.example.nursingsystem.knowledge.dto.ZeroAccessWarningDTO;
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

    /**
     * 下载资源
     * @param resourceId 资源 ID
     * @param userId 用户 ID
     * @param userName 用户名
     * @param downloadIp 下载 IP
     * @return 文件下载信息
     */
    String downloadResource(Long resourceId, Long userId, String userName, String downloadIp);

    /**
     * 预览资源
     * @param resourceId 资源 ID
     * @return 文件预览 URL 或内容
     */
    String previewResource(Long resourceId);

    /**
     * 获取热门资源排行（按浏览量）
     * @param limit 返回数量限制
     * @return 热门资源列表
     */
    java.util.List<HotKnowledgeResourceDTO> getTopViewedResources(int limit);

    /**
     * 获取热门资源排行（按下载量）
     * @param limit 返回数量限制
     * @return 热门资源列表
     */
    java.util.List<HotKnowledgeResourceDTO> getTopDownloadedResources(int limit);

    /**
     * 获取零访问预警资源列表
     * @param days 天数阈值（超过该天数未访问的资源）
     * @param limit 返回数量限制
     * @return 零访问预警资源列表
     */
    java.util.List<ZeroAccessWarningDTO> getZeroAccessWarnings(int days, int limit);
}