package com.application.security.infra.exception;

public class AuthenticationSecurityException extends SecurityException {

    public AuthenticationSecurityException(String message) {
        super(message);
    }

    public AuthenticationSecurityException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
