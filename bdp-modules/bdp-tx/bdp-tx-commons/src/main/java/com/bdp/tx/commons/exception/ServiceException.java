package com.bdp.tx.commons.exception;

/**
 * 业务异常
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
