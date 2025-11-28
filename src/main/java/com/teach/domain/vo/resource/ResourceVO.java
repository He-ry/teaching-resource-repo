package com.teach.domain.vo.resource;

import com.teach.domain.bo.ResourceTypeBo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceVO {

    @Schema(description = "资源ID")
    private Long id;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "资源标题")
    private String title;

    @Schema(description = "资源描述")
    private String description;

    @Schema(description = "资源内容")
    private String content;

    @Schema(description = "浏览量")
    private Long viewCount;

    @Schema(description = "创建时间")
    private String createdAt;

    @Schema(description = "更新时间")
    private String updatedAt;

    @Schema(description = "资源类型信息")
    private ResourceTypeBo type;
}
