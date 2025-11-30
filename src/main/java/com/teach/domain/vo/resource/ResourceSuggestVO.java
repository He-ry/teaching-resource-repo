package com.teach.domain.vo.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceSuggestVO {

    private List<Resources> resources;

    private Integer count;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Resources {

        private String id;

        private String title;

        private String description;

        @JsonProperty("view_count")
        private Integer viewCount;
    }
}
