package com.teach.domain.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FileSaveDTO {

    @Schema(description = "文件ID")
    private String id;

    @Schema(description = "文件Hash")
    private String hash;

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件大小 (MB)")
    private Double size;

    @Schema(description = "时长")
    private String duration;

    @Schema(description = "文件图片")
    private String cover;

    @Schema(description = "文件类型")
    private String kind;

    @Schema(description = "文件保存地址")
    private String url;

    @Schema(description = "文件后缀")
    private String suffix;
}
