package com.lorne.core.framework.exception;

/**
 * feign 拦截异常
 * create by lorne on 2017/9/27
 */
public class FeignErrorException extends LEException{

    public FeignErrorException() {
    }

    public FeignErrorException(String message) {
        super(message);
    }

    public FeignErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeignErrorException(Throwable cause) {
        super(cause);
    }

    public FeignErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
