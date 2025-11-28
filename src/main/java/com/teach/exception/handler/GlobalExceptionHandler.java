package com.tracker.framework.exception.handler;

import cn.dev33.satoken.exception.NotLoginException;
import com.tracker.framework.domain.Result;
import com.tracker.framework.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

import static com.tracker.framework.exception.enums.StatusCodeEnum.*;


/**
 * @author He
 * @version 1.0
 * @Date 2023/9/7 9:49
 * @Desc 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理登录异常
     */
    @ExceptionHandler(value = NotLoginException.class)
    public Result<?> handleLoginException(NotLoginException nle) {
        String message = switch (nle.getType()) {
            case NotLoginException.NOT_TOKEN -> "未能读取到有效 token";
            case NotLoginException.INVALID_TOKEN -> "token 无效";
            case NotLoginException.TOKEN_TIMEOUT -> "token 已过期";
            case NotLoginException.BE_REPLACED -> "token 已被顶下线";
            case NotLoginException.KICK_OUT -> "token 已被踢下线";
            case NotLoginException.TOKEN_FREEZE -> "token 已被冻结";
            case NotLoginException.NO_PREFIX -> "未按照指定前缀提交 token";
            default -> "当前会话未登录";
        };
        return Result.fail(message);
    }


    /**
     * 处理业务异常
     */
    @ExceptionHandler(value = ServiceException.class)
    public Result<?> handleServiceException(ServiceException e) {
        return Result.fail(e.getMessage());
    }

    /**
     * 处理Assert异常
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return Result.fail(e.getMessage());
    }

    /**
     * 处理参数校验异常(@Valid)
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Result.fail(VALID_ERROR.getCode(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * 处理参数校验异常(@Validated)
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result<?> handleValidationException(ConstraintViolationException e) {
//        if (!StringUtils.isEmpty(e.getMessage())) {
//            return Result.fail(VALID_ERROR.getCode(),e.getConstraintViolations().toString());
//        }
        return Result.fail(VALID_ERROR);
    }

    /**
     * 处理非法JSON格式异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return Result.fail(JSON_FORMAT_ERROR.getCode(), JSON_FORMAT_ERROR.getMsg());
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> handleSystemException(Exception e) {
        log.error("系统异常:{e}", e);
        return Result.fail(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getMsg());
    }


}
