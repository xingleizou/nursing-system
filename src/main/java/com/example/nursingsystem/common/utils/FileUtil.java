package com.example.nursingsystem.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件工具类
 */
public class FileUtil {

    /**
     * 生成唯一的文件名
     */
    public static String generateFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        return UUID.randomUUID().toString().replace("-", "") + "_" + 
               System.currentTimeMillis() + extension;
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    /**
     * 上传文件
     */
    public static String uploadFile(MultipartFile file, String uploadDir) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 创建上传目录
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // 生成唯一文件名
        String fileName = generateFileName(file.getOriginalFilename());
        Path filePath = path.resolve(fileName);

        // 保存文件
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 检查文件是否存在
     */
    public static boolean exists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
}
