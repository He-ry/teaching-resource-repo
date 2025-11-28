package com.teach.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceSubTypeEnum {

    NATIONAL("national", "国家标准"),
    POLICY("policy", "政策文件"),
    PLAN("plan", "方案计划"),
    REPORT("report", "研究报告"),
    ARTICLE("article", "学术文章"),
    THESIS("thesis", "学位论文"),
    THEORY("theory", "理论教育"),
    PRACTICE("practice", "实践案例"),
    TECHNICAL("technical", "技术手段");

    private final String code;
    private final String label;

    public static ResourceSubTypeEnum fromCode(String code) {
        for (ResourceSubTypeEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return NATIONAL;
    }
}
