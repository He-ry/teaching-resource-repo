package com.teach.domain.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceSaveDTO {

    @Schema(description = "资源ID，可选（更新时使用）")
    private Long id;

    @NotBlank(message = "资源标题不能为空")
    @Schema(description = "资源标题")
    private String title;

    @Schema(description = "资源描述")
    private String description;

    @Schema(description = "资源内容")
    private String content;

    @NotNull(message = "资源类型不能为空")
    @Schema(description = "资源类型对象")
    private ResourceType type;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "资源类型信息")
    public static class ResourceType {

        @NotBlank(message = "一级类型不能为空")
        @Schema(description = "一级类型", example = "institutional_norm")
        private String type;

        @NotBlank(message = "二级类型不能为空")
        @Schema(description = "二级类型", example = "national")
        private String subType;

        @NotBlank(message = "三级类型不能为空")
        @Schema(description = "三级类型", example = "project")
        private String subSubType;
    }
}
