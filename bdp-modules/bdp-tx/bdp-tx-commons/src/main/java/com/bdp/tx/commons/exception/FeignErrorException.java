package com.bdp.tx.commons.exception;

/**
 * feign 拦截异常
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
