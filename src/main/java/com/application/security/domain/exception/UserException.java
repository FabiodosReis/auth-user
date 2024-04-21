package com.application.security.domain.exception;


public class UserException extends AuthApiException {

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
