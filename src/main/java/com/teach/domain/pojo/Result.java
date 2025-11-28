package com.teach.domain.pojo;

import com.teach.exception.enums.StatusCodeEnum;
import com.teach.exception.enums.StatusCodeProvider;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import static com.teach.exception.enums.StatusCodeEnum.FAIL;


/**
 * @author He
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;


    public Result() {
        this.code = StatusCodeEnum.SUCCESS.getCode();
        this.msg = StatusCodeEnum.SUCCESS.getMsg();
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(Integer code, String msg, T data, Long total) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(Integer code, String msg, T data, Long total, Integer pageNum, Integer pageSize) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    /**
     * 成功（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getMsg(), null);
    }

    /**
     * 成功（含数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getMsg(), data);
    }

    /**
     * 成功（分页数据）
     */
    public static <T> Result<List<T>> page(List<T> records, long total) {
        return new Result<>(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getMsg(), records, total);
    }

    /**
     * 成功（分页数据 + 页码信息）
     */
    public static <T> Result<List<T>> page(List<T> records, long total, int pageNum, int pageSize) {
        return new Result<>(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getMsg(), records, total, pageNum, pageSize);
    }

    /**
     * 失败（消息）
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(FAIL.getCode(), message, null);
    }

    /**
     * 失败（自定义状态码和消息）
     */
    public static <T> Result<T> fail(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }

    /**
     * 失败（使用枚举）
     */
    public static <T, E extends Enum<E> & StatusCodeProvider> Result<T> fail(E enumValue) {
        return new Result<>(enumValue.getCode(), enumValue.getMsg(), null);
    }
}
