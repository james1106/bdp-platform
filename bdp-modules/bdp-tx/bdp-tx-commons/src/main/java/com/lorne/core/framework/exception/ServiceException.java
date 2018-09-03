package com.lorne.core.framework.exception;

/**
 * 业务异常
 * Created by yuliang on 2017/4/7.
 */
public class ServiceException extends LEException {

    public ServiceException() {

    }

    public ServiceException(String message) {
        super(message);

    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);

    }



}
