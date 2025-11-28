package com.tracker.framework.exception.enums;

/**
 * @author He
 * @version 1.0
 * &#064;Desc  接口用于获取状态码和消息的提供者 任何实现此接口的类或枚举应该提供状态码和消息的获取方法。
 */
public interface StatusCodeProvider {

    /**
     * 获取状态码。
     *
     * @return 状态码的整数表示。
     */
    Integer getCode();

    /**
     * 获取状态消息。
     *
     * @return 状态消息的字符串表示。
     */
    String getMsg();
}

