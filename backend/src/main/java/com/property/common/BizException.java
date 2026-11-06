package com.property.common;

/**
 * 业务异常，service 层直接抛出，由全局异常处理器统一转换成返回结果
 */
public class BizException extends RuntimeException {

    private final int code;

    public BizException(String msg) {
        super(msg);
        this.code = 500;
    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
