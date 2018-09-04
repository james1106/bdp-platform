package com.bdp.tx.commons.exception;

/**
 * 事务异常
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
