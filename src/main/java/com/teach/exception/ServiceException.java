package com.tracker.framework.exception;


import static com.tracker.framework.exception.enums.StatusCodeEnum.FAIL;

public class ServiceException extends RuntimeException{
    /**
     * 返回失败状态码
     */
    private final Integer code = FAIL.getCode();

    /**
     * 返回信息
     */
    private final String message;

    public ServiceException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}