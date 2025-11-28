package com.teach.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceSubSubTypeEnum {

    PROJECT("project", "立项书"),
    PROPOSAL("proposal", "开题报告书"),
    REVIEW("review", "综述类"),
    THEORETICAL("theoretical", "理论类"),
    TECHNICAL("technical", "科技类"),
    MILITARY("military", "优秀军事类"),
    SCIENCE("science", "优秀理工类"),
    OTHER("other", "其它");

    private final String code;
    private final String label;

    public static ResourceSubSubTypeEnum fromCode(String code) {
        for (ResourceSubSubTypeEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return OTHER;
    }
}
