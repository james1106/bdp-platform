package com.bdp.tx.commons.exception;

public class SocketException extends IOException{

    public SocketException() {
    }

    public SocketException(String message) {
        super(message);
    }

    public SocketException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketException(Throwable cause) {
        super(cause);
    }

    public SocketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
