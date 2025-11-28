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
@TableName("t_file")
@Schema(name = "TFile", description = "文件表")
@JsonIgnoreProperties(value = "transMap")
public class FileDO implements Serializable, TransPojo {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "文件ID")
    @TableId("id")
    private Long id;

    @Schema(description = "文件Hash")
    @TableField("hash")
    private String hash;

    @Schema(description = "文件名称")
    @TableField("name")
    private String name;

    @Schema(description = "文件大小 (MB)")
    @TableField("size")
    private Double size;

    @Schema(description = "时长")
    @TableField("duration")
    private String duration;

    @Schema(description = "文件图片")
    @TableField("cover")
    private String cover;

    @Schema(description = "文件类型")
    @TableField("kind")
    private String kind;

    @Schema(description = "文件保存地址")
    @TableField("url")
    private String url;

    @Schema(description = "文件后缀")
    @TableField("suffix")
    private String suffix;

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
