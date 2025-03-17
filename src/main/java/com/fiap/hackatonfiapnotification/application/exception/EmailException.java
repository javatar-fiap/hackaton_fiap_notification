package com.fiap.hackatonfiapnotification.application.exception;

public class EmailException extends RuntimeException {
    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
