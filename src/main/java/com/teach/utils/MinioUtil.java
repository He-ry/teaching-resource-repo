package com.tracker.framework.utils;

import cn.hutool.core.util.StrUtil;
import com.tracker.framework.config.minio.MinioConfig;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Slf4j
@Component
public class MinioUtil {

    @Resource
    private MinioConfig minioConfig;

    @Resource
    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
            }
        } catch (Exception e) {
            log.error("创建minio桶失败", e);
        }
    }

    /**
     * 上传 MultipartFile 文件
     */
    public FileInfo uploadFile(MultipartFile file){
        String bucketName = minioConfig.getBucketName();
        String originalFileName = StrUtil.isBlank(file.getOriginalFilename()) ? "unknow" : file.getOriginalFilename();
        String objectName = generateFilePath(bucketName, originalFileName);
        String suffix = originalFileName.contains(".") ? originalFileName.substring(originalFileName.lastIndexOf(".") + 1) : "";
        String url = minioConfig.getEndpoint() + StrUtil.SLASH + bucketName + StrUtil.SLASH + objectName;
        try(InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            log.error("上传文件失败", e);
        }
        return FileInfo.builder()
                .name(originalFileName)
                .objectName(objectName)
                .url(url)
                .size(file.getSize())
                .contentType(file.getContentType())
                .suffix(suffix)
                .build();
    }


    /**
     * 上传 InputStream 文件
     */
    public FileInfo uploadFile(InputStream inputStream, @NotNull String name) {
        String bucketName = minioConfig.getBucketName();
        String contentType = "application/octet-stream";
        long size = 0;
        String objectName = generateFilePath(bucketName, name);
        String url = minioConfig.getEndpoint() + StrUtil.SLASH + bucketName + StrUtil.SLASH + objectName;
        String suffix = name.contains(".") ? name.substring(name.lastIndexOf(".") + 1) : "";
        try(inputStream) {
            size = inputStream.available();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            log.error("上传文件失败", e);
        }
        return FileInfo.builder()
                .name(name)
                .objectName(objectName)
                .url(url)
                .size(size)
                .contentType(contentType)
                .suffix(suffix)
                .build();
    }




    /**
     * 生成文件路径: 桶名/yyyyMMdd/uuid_文件名
     */
    private String generateFilePath(String bucketName, String originalFileName) {
        String datePath = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        return bucketName + StrUtil.SLASH + datePath + StrUtil.SLASH + UUID.randomUUID() + "_" + originalFileName;
    }


    /**
     * 删除文件
     */
    public void deleteFile(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    /**
     * 批量删除文件
     */
    public void deleteAllObjectsUnderPath(String bucketName, String prefix) {
        try {
            // 列出所有对象
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .recursive(true)
                            .build()
            );

            List<DeleteObject> objectsToDelete = new ArrayList<>();
            for (Result<Item> result : results) {
                objectsToDelete.add(new DeleteObject(result.get().objectName()));
            }

            if (objectsToDelete.isEmpty()) {
                log.info("没有找到以 '{}' 为前缀的对象", prefix);
                return;
            }

            // 批量删除对象
            Iterable<Result<DeleteError>> errors = minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucketName)
                            .objects(objectsToDelete)
                            .build()
            );

            // 处理删除错误
            int failCount = 0;
            for (Result<DeleteError> errorResult : errors) {
                try {
                    DeleteError error = errorResult.get();
                    log.error("删除失败: {}，原因: {}", error.objectName(), error.message());
                    failCount++;
                } catch (Exception e) {
                    log.error("获取删除错误信息失败", e);
                    failCount++;
                }
            }

            log.info("尝试删除 {} 个对象, 失败 {} 个", objectsToDelete.size(), failCount);

        } catch (MinioException e) {
            log.error("MinIO 操作失败: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("删除对象时发生异常: {}", e.getMessage(), e);
        }
    }


    /**
     * 下载文件
     */
    public InputStream downloadFile(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    /**
     * 文件信息类
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        private  String name;
        private  String objectName;
        private  String url;
        private  Long size;
        private  String contentType;
        private  String suffix;
    }
}
