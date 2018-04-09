package com.torstensommerfeld.utils.exceptions;

public class InvalidMatrixOperation extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidMatrixOperation() {
        super();
    }

    public InvalidMatrixOperation(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidMatrixOperation(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMatrixOperation(String message) {
        super(message);
    }

    public InvalidMatrixOperation(String message, Object... params) {
        super(String.format(message, params));
    }

    public InvalidMatrixOperation(Throwable cause) {
        super(cause);
    }

}
