package com.example.nursingsystem.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nursingsystem.knowledge.entity.KnowledgeResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 知识资源 Mapper 接口
 */
@Mapper
public interface KnowledgeResourceMapper extends BaseMapper<KnowledgeResource> {

    /**
     * 根据 MD5 值查询资源 (用于去重)
     * @param md5Code 文件 MD5 值
     * @return 知识资源
     */
    KnowledgeResource selectByMd5(@Param("md5Code") String md5Code);

    /**
     * 增加下载次数
     * @param resourceId 资源 ID
     * @return 影响行数
     */
    int incrementDownloadCount(@Param("resourceId") Long resourceId);

    /**
     * 增加浏览次数
     * @param resourceId 资源 ID
     * @return 影响行数
     */
    int incrementViewCount(@Param("resourceId") Long resourceId);

    /**
     * 查询热门资源（按浏览量）
     * @param limit 返回数量限制
     * @return 热门资源列表
     */
    java.util.List<java.util.Map<String, Object>> selectTopViewedResources(@Param("limit") int limit);

    /**
     * 查询热门资源（按下载量）
     * @param limit 返回数量限制
     * @return 热门资源列表
     */
    java.util.List<java.util.Map<String, Object>> selectTopDownloadedResources(@Param("limit") int limit);

    /**
     * 查询零访问预警资源
     * @param thresholdTime 时间阈值
     * @param limit 返回数量限制
     * @return 零访问预警资源列表
     */
    java.util.List<java.util.Map<String, Object>> selectZeroAccessWarnings(
            @Param("thresholdTime") java.time.LocalDateTime thresholdTime,
            @Param("limit") int limit);
}
