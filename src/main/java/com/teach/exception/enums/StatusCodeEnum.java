package com.tracker.framework.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author He
 * @version 1.0
 * @Date 2023/9/6 17:08
 * @Desc 状态码枚举
 */
@Getter
@AllArgsConstructor
public enum StatusCodeEnum implements StatusCodeProvider {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 参数错误
     */
    VALID_ERROR(400, "参数错误"),

    /**
     * JSON格式错误
     */
    JSON_FORMAT_ERROR(400, "JSON格式错误"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(-1, "系统异常"),

    /**
     * 操作失败
     */
    FAIL(500, "操作失败"),

    /**
     * 请求过于频繁
     */
    TOO_MANY_REQUESTS(429, "请求过于频繁");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 返回信息
     */
    private final String msg;
}
