package com.teach.controller;

import com.teach.domain.pojo.PageResult;
import com.teach.domain.pojo.Result;
import com.teach.domain.vo.file.FileVO;
import com.teach.domain.vo.file.ImageFileUploadVO;
import com.teach.domain.vo.file.VideoFileUploadVO;
import com.teach.service.file.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;


@Tag(name = "文件管理")
@RestController
@RequestMapping("/api/v1/editor")
@Validated
public class ImageController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "图片上传")
    public HashMap<String, Object> createFile(@RequestParam("wangeditor-uploaded-image") MultipartFile file) {
        ImageFileUploadVO res = fileService.createFile(file);
        HashMap<String, Object> map = new HashMap<>();
        map.put("errno", 0);
        map.put("data", res);
        return map;
    }

    @PostMapping("/upload-video")
    @Operation(summary = "视频上传")
    public HashMap<String, Object> createFileVideo(@RequestParam("wangeditor-uploaded-video") MultipartFile file) {
        VideoFileUploadVO res = fileService.createVideoFile(file);
        HashMap<String, Object> map = new HashMap<>();
        map.put("errno", 0);
        map.put("data", res);
        return map;
    }

    @GetMapping("/images")
    @Operation(summary = "获取图片列表")
    public HashMap<String, Object> images(@RequestParam("page") Integer page, @RequestParam("per_page") Integer pageSize) {
        PageResult<FileVO> res = fileService.images(page, pageSize, "image");
        HashMap<String, Object> map = new HashMap<>();
        map.put("errno", 0);
        map.put("data", res);
        return map;
    }

    @GetMapping("/videos")
    @Operation(summary = "获取视频列表")
    public HashMap<String, Object> video(@RequestParam("page") Integer page, @RequestParam("per_page") Integer pageSize) {
        PageResult<FileVO> res = fileService.images(page, pageSize, "video");
        HashMap<String, Object> map = new HashMap<>();
        map.put("errno", 0);
        map.put("data", res);
        return map;
    }

    @DeleteMapping("/images/{image_id}")
    @Operation(summary = "删除图片")
    @Parameter(name = "image_id", description = "文件ID", required = true)
    public HashMap<String, Object> deleteFile(@PathVariable("image_id") String id) {
        fileService.deleteFile(id);
        return new HashMap<>();
    }

    @DeleteMapping("/videos/{video_id}")
    @Operation(summary = "删除视频")
    @Parameter(name = "id", description = "视频id", required = true)
    public HashMap<String, Object> deleteFileVideo(@PathVariable("video_id") String id) {
        fileService.deleteFile(id);
        return new HashMap<>();
    }
}
