package com.teach.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.teach.config.minio.MinioConfig;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Component
public class MinioUtil {

    @Resource
    private MinioConfig minioConfig;

    @Resource
    private MinioClient minioClient;

    private static final Map<String, String> MIME_TYPE_MAP = new HashMap();

    static {
        MIME_TYPE_MAP.put("jpg", "image/jpeg");
        MIME_TYPE_MAP.put("jpeg", "image/jpeg");
        MIME_TYPE_MAP.put("png", "image/png");
        MIME_TYPE_MAP.put("gif", "image/gif");
        MIME_TYPE_MAP.put("mp4", "video/mp4");
        MIME_TYPE_MAP.put("avi", "video/x-msvideo");
        MIME_TYPE_MAP.put("wmv", "video/x-ms-wmv");
        MIME_TYPE_MAP.put("mpg", "video/mpeg");
        MIME_TYPE_MAP.put("mov", "video/quicktime");
        MIME_TYPE_MAP.put("rm", "application/vnd.rn-realmedia");
        MIME_TYPE_MAP.put("swf", "application/x-shockwave-flash");
        MIME_TYPE_MAP.put("flv", "video/x-flv");
        MIME_TYPE_MAP.put("mkv", "video/x-matroska");
        MIME_TYPE_MAP.put("ico", "image/x-icon");
        MIME_TYPE_MAP.put("zip", "application/zip");
        MIME_TYPE_MAP.put("7z", "application/x-7z-compressed");
        MIME_TYPE_MAP.put("tar", "application/x-tar");
        MIME_TYPE_MAP.put("gz", "application/gzip");
        MIME_TYPE_MAP.put("bz2", "application/x-bzip2");
        MIME_TYPE_MAP.put("pdf", "application/pdf");
        MIME_TYPE_MAP.put("txt", "text/plain");
        MIME_TYPE_MAP.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        MIME_TYPE_MAP.put("doc", "application/msword");
        MIME_TYPE_MAP.put("xls", "application/vnd.ms-excel");
        MIME_TYPE_MAP.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public String getMimeType(String fileName) {
        String extension = this.getFileExtension(fileName);
        return (String)MIME_TYPE_MAP.getOrDefault(extension, "application/octet-stream");
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(46);
        return lastDotIndex != -1 && lastDotIndex != fileName.length() - 1 ? fileName.substring(lastDotIndex + 1).toLowerCase() : "";
    }

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
                            .contentType(this.getMimeType(originalFileName))
                            .build()
            );
        } catch (Exception e) {
            log.error("上传文件失败", e);
        }
        String videoCover = "";
        if (isAudioFile(FileNameUtil.extName(originalFileName))) {
            File tempFile = FileUtil.createTempFile(UUID.randomUUID().toString(), true);
            videoCover = getVideoCover(tempFile);
        }
        return FileInfo.builder()
                .name(originalFileName)
                .objectName(objectName)
                .url(url)
                .cover(videoCover)
                .size(file.getSize())
                .contentType(file.getContentType())
                .suffix(suffix)
                .build();
    }

    public String getVideoCover(File videoFile) {
        String coverUrl = "";
        String coverName = videoFile.getName().substring(0, videoFile.getName().lastIndexOf('.')) + "_cover.jpg";

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFile)) {
            grabber.start();

            // 跳到第 1 秒
            grabber.setTimestamp(1_000_000);

            Frame frame = grabber.grabImage();
            if (frame == null) {
                log.warn("未能抓取到图像帧，尝试前几帧");
                for (int i = 0; i < 5 && frame == null; i++) {
                    frame = grabber.grabImage();
                }
            }
            if (frame != null) {
                // 转为 BufferedImage
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bi = converter.convert(frame);
                // 写入到 ByteArrayOutputStream
                try (ByteArrayOutputStream bass = new ByteArrayOutputStream()) {
                    ImageIO.write(bi, "jpg", bass);
                    ByteArrayInputStream basis = new ByteArrayInputStream(bass.toByteArray());
                    // 上传到 MinIO
                    FileInfo fileInfo = uploadFile(basis, coverName);
                    coverUrl = fileInfo.getUrl();
                    log.info("封面上传成功: {}", coverUrl);
                }
            } else {
                log.error("未能抓取到有效视频帧: {}", videoFile.getAbsolutePath());
            }

            grabber.stop();
        } catch (Exception e) {
            log.error("生成视频封面失败", e);
        }
        return coverUrl;
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

    private static boolean isAudioFile(String fileExt) {
        String[] audioExtensions = new String[]{"wav", "flac", "aac", "mp4", "aiff", "mp3", "wma", "avi", "ogg", "m4a", "mpeg", "wmv", "mov", "mkv"};

        for(String ext : audioExtensions) {
            if (fileExt.endsWith(ext)) {
                return true;
            }
        }

        return false;
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
        private  String cover;
    }
}
