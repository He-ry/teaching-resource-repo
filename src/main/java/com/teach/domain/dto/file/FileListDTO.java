package com.teach.domain.dto.file;

import com.teach.domain.pojo.SortablePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FileListDTO extends SortablePageParam {


    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件类型")
    private String kind;
}
