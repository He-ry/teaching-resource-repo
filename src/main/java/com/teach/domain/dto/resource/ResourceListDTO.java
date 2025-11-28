package com.teach.domain.dto.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teach.domain.pojo.SortablePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResourceListDTO extends SortablePageParam {


    @Schema(description = "第一级类型")
    private String type;

    @Schema(description = "第二级类型")
    @JsonProperty("sub_type")
    private String subType;

    @Schema(description = "第三级类型")
    @JsonProperty("sub_sub_type")
    private String subSubType;
    
    @Schema(description = "搜索关键字 名称/描述")
    private String search;
}
