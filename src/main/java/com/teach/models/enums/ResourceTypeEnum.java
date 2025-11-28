package com.teach.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {

    INSTITUTIONAL_NORM("institutional_norm", "制度规范"),
    RESEARCH_RESULTS("research_results", "研究成果"),
    IDEOLOGICAL("ideological", "思政资源"),
    PRACTICAL_METHOD("practical_method", "实用方法");

    private final String code;
    private final String label;

    public static ResourceTypeEnum fromCode(String code) {
        for (ResourceTypeEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return INSTITUTIONAL_NORM;
    }
}
