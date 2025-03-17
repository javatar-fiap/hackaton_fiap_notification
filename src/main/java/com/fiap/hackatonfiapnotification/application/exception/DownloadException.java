package com.fiap.hackatonfiapnotification.application.exception;

public class DownloadException extends RuntimeException {
    public DownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}