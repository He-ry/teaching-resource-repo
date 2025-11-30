package com.teach.domain.vo.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FileVO", description = "文件信息")
public class FileVO {

    @Schema(description = "文件ID")
    private Long id;

    @Schema(description = "文件原始名称")
    @JsonProperty("original_name")
    private String originalName;

    @Schema(description = "文件名")
    private String filename;

    @Schema(description = "文件路径")
    @JsonProperty("file_path")
    private String filePath;

    @Schema(description = "文件大小（字节）")
    @JsonProperty("file_size")
    private Long fileSize;

    @Schema(description = "MIME类型")
    @JsonProperty("mime_type")
    private String mimeType;

    @Schema(description = "文件访问URL")
    private String url;

    @Schema(description = "文件访问href")
    private String href;

    @Schema(description = "alt文本")
    @JsonProperty("alt_text")
    private String altText;

    @Schema(description = "创建时间")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}