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
}
