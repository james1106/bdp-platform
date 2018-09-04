package com.bdp.tx.commons.exception;

/**
 * 参数校验异常
 */
public class ParamException extends ServiceException {

    public ParamException() {
    }

    public ParamException(String message) {
        super(message);
    }


    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(Throwable cause) {
        super(cause);
    }

}
