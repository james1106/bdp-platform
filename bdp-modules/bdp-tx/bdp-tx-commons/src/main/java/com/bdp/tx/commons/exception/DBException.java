package com.bdp.tx.commons.exception;

/**
 * DB层业务异常
 */
public class DBException extends ServiceException {

    public DBException() {
    }

    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(Throwable cause) {
        super(cause);
    }


}
