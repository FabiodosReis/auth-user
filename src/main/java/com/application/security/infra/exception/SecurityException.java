package com.application.security.infra.exception;


public abstract class SecurityException extends RuntimeException {

    protected SecurityException(String message){
        super(message);
    }

    protected SecurityException(String message, Throwable throwable){
        super(message, throwable);
    }
}
