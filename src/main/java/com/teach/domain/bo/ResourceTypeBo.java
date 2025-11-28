package com.teach.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceTypeBo {

    @Schema(description = "一级类型", example = "institutional_norm")
    private String type;

    @Schema(description = "二级类型", example = "national")
    private String subType;

    @Schema(description = "三级类型", example = "project")
    private String subSubType;
}
