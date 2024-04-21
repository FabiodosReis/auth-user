package com.application.security.domain.exception;


public abstract class AuthApiException extends RuntimeException {

    protected AuthApiException(String message){
        super(message);
    }

    protected AuthApiException(String message, Throwable throwable){
        super(message, throwable);
    }
}
