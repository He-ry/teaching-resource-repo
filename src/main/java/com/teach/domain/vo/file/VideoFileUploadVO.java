package com.teach.domain.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoFileUploadVO {

    @Schema(description = "poster")
    private String poster;

    @Schema(description = "url")
    private String url;

}
