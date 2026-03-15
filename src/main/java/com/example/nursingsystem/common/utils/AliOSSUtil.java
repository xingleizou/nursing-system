package com.example.nursingsystem.common.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.example.nursingsystem.properties.AliOssProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 阿里云 OSS 文件上传工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliOSSUtil {

    private final AliOssProperties aliOssProperties;

    /**
     * 上传文件到 OSS
     *
     * @param file 上传的文件
     * @return OSS 中的文件路径（URL）
     * @throws IOException IO 异常
     */
    public String upload(MultipartFile file) throws IOException {
        // 获取原始文件名和扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : "";
        
        // 生成唯一的文件名
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String objectName = datePath + "/" + uuid + extension;

        // 创建 OSS 客户端实例
        OSS ossClient = new OSSClientBuilder().build(
                aliOssProperties.getEndpoint(), 
                aliOssProperties.getAccessKeyId(), 
                aliOssProperties.getAccessKeySecret());

        try {
            // 设置元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // 上传文件
            InputStream inputStream = new ByteArrayInputStream(file.getBytes());
            ossClient.putObject(aliOssProperties.getBucketName(), objectName, inputStream, metadata);

            log.info("文件上传成功，Bucket: {}, Object: {}", aliOssProperties.getBucketName(), objectName);
            
            // 返回文件访问 URL
            return "https://" + aliOssProperties.getBucketName() + "." + getEndpointHost() + "/" + objectName;
        } catch (Exception e) {
            log.error("OSS 上传失败", e);
            throw new IOException("OSS 上传失败：" + e.getMessage(), e);
        } finally {
            // 关闭 OSS 客户端
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 上传字节数组到 OSS
     *
     * @param bytes 文件字节数组
     * @param originalFilename 原始文件名
     * @return OSS 中的文件路径（URL）
     * @throws IOException IO 异常
     */
    public String upload(byte[] bytes, String originalFilename) throws IOException {
        String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : "";
        
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String objectName = datePath + "/" + uuid + extension;

        OSS ossClient = new OSSClientBuilder().build(
                aliOssProperties.getEndpoint(), 
                aliOssProperties.getAccessKeyId(), 
                aliOssProperties.getAccessKeySecret());

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            
            InputStream inputStream = new ByteArrayInputStream(bytes);
            ossClient.putObject(aliOssProperties.getBucketName(), objectName, inputStream, metadata);

            log.info("文件上传成功，Bucket: {}, Object: {}", aliOssProperties.getBucketName(), objectName);
            return "https://" + aliOssProperties.getBucketName() + "." + getEndpointHost() + "/" + objectName;
        } catch (Exception e) {
            log.error("OSS 上传失败", e);
            throw new IOException("OSS 上传失败：" + e.getMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 删除 OSS 中的文件
     *
     * @param fileUrl 文件 URL
     * @throws IOException IO 异常
     */
    public void delete(String fileUrl) throws IOException {
        try {
            // 从 URL 中提取 objectName
            String host = getEndpointHost();
            String path = fileUrl.replace("https://" + aliOssProperties.getBucketName() + "." + host + "/", "");
            
            OSS ossClient = new OSSClientBuilder().build(
                    aliOssProperties.getEndpoint(), 
                    aliOssProperties.getAccessKeyId(), 
                    aliOssProperties.getAccessKeySecret());
            
            try {
                ossClient.deleteObject(aliOssProperties.getBucketName(), path);
                log.info("文件删除成功，Bucket: {}, Object: {}", aliOssProperties.getBucketName(), path);
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        } catch (Exception e) {
            log.error("OSS 删除文件失败", e);
            throw new IOException("OSS 删除文件失败：" + e.getMessage(), e);
        }
    }

    /**
     * 从 endpoint 提取 host
     */
    private String getEndpointHost() {
        // 例如：oss-cn-beijing.aliyuncs.com -> oss-cn-beijing.aliyuncs.com
        return aliOssProperties.getEndpoint().replaceFirst("^https?://", "");
    }
}
