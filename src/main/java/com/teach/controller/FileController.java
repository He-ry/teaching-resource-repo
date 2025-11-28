package com.teach.controller;

import com.teach.domain.pojo.Result;
import com.teach.domain.dto.file.FileSaveDTO;
import com.teach.models.entity.FileDO;
import com.teach.service.file.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "文件管理")
@RestController
@RequestMapping("/api/v1/file")
@Validated
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/create")
    @Operation(summary = "创建文件")
    public Result<Long> createFile(@Valid @RequestBody FileSaveDTO dto) {
        return Result.success(fileService.createFile(dto));
    }

    @PutMapping("/update")
    @Operation(summary = "更新文件")
    public Result<Boolean> updateFile(@Valid @RequestBody FileSaveDTO dto) {
        fileService.updateFile(dto);
        return Result.success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文件")
    @Parameter(name = "id", description = "文件ID", required = true)
    public Result<Boolean> deleteFile(@RequestParam("id") Long id) {
        fileService.deleteFile(id);
        return Result.success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除文件")
    public Result<Boolean> deleteFileList(@RequestParam("ids") List<Long> ids) {
        fileService.deleteFileListByIds(ids);
        return Result.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获取文件详情")
    public Result<FileDO> getFile(@RequestParam("id") Long id) {
        return Result.success(fileService.getFile(id));
    }

}
