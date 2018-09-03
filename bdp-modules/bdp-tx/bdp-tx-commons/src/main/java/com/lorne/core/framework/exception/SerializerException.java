package com.lorne.core.framework.exception;

/**
 * 序列化异常
 * create by lorne on 2017/9/27
 */
public class SerializerException extends ServiceException{

    public SerializerException() {
    }

    public SerializerException(String message) {
        super(message);
    }

    public SerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializerException(Throwable cause) {
        super(cause);
    }
}
