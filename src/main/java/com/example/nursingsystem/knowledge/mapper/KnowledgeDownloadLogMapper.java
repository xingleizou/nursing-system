package com.example.nursingsystem.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nursingsystem.knowledge.entity.KnowledgeDownloadLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 下载日志 Mapper 接口
 */
@Mapper
public interface KnowledgeDownloadLogMapper extends BaseMapper<KnowledgeDownloadLog> {
}
