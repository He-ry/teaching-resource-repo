package com.teach.models.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dromara.core.trans.vo.TransPojo;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("t_resource")
@Schema(name = "TResource", description = "资源表")
@JsonIgnoreProperties(value = "transMap")
public class TResource implements Serializable, TransPojo {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "资源ID")
    @TableId("id")
    private Long id;

    @Schema(description = "作者")
    @TableField("author")
    private String author;

    @Schema(description = "资源标题")
    @TableField("title")
    private String title;

    @Schema(description = "资源描述")
    @TableField("description")
    private String description;

    @Schema(description = "资源内容")
    @TableField("content")
    private String content;

    @Schema(description = "浏览量")
    @TableField("view_count")
    private Integer viewCount;

    @Schema(description = "第一级类型")
    @TableField("type")
    private String type;

    @Schema(description = "第二级类型")
    @TableField("sub_type")
    private String subType;

    @Schema(description = "第三级类型")
    @TableField("sub_sub_type")
    private String subSubType;

    @Schema(description = "创建人")
    @TableField("created_by")
    private String createdBy;

    @Schema(description = "更新人")
    @TableField("updated_by")
    private String updatedBy;

    @Schema(description = "创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @Schema(description = "逻辑删除")
    @TableField("deleted")
    private Boolean deleted;

}
