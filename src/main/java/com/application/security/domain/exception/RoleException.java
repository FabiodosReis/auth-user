package com.application.security.domain.exception;


public class RoleException extends AuthApiException {


    public RoleException(String message) {
        super(message);
    }

    public RoleException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
