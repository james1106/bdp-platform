package com.lorne.core.framework.exception;

/**
 * 事务异常
 * create by lorne on 2017/9/27
 */
public class TransactionException extends ServiceException{

    public TransactionException() {
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
