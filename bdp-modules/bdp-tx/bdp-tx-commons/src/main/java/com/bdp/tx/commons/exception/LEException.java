package com.bdp.tx.commons.exception;

public class LEException extends Exception {

    public LEException() {
    }

    public LEException(String message) {
        super(message);
    }

    public LEException(String message, Throwable cause) {
        super(message, cause);
    }

    public LEException(Throwable cause) {
        super(cause);
    }

    public LEException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
