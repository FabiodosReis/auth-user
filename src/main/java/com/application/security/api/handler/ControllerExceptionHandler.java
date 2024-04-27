package com.application.security.api.handler;

import com.application.security.domain.exception.AuthApiException;
import com.application.security.domain.exception.RoleException;
import com.application.security.domain.exception.UserException;
import com.application.security.infra.exception.AuthenticationSecurityException;
import com.application.security.infra.exception.SecurityException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.time.LocalDateTime;


@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {RoleException.class, UserException.class})
    public ResponseEntity<?> authHandlerException(AuthApiException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        DetailError.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .date(LocalDateTime.now())
                                .message(exception.getMessage())
                                .build()
                );

    }

    @ExceptionHandler(value = {AuthenticationSecurityException.class})
    public ResponseEntity<?> authHandlerException(SecurityException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        DetailError.builder()
                                .code(HttpStatus.FORBIDDEN.value())
                                .date(LocalDateTime.now())
                                .message(exception.getMessage())
                                .build()
                );

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerExceptionResolver(Exception exception) {

        if (exception instanceof BadCredentialsException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(
                            DetailError.builder()
                                    .code(HttpStatus.FORBIDDEN.value())
                                    .date(LocalDateTime.now())
                                    .message("Authentication failure")
                                    .build()
                    );
        }

        if (exception instanceof AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(
                            DetailError.builder()
                                    .code(HttpStatus.FORBIDDEN.value())
                                    .date(LocalDateTime.now())
                                    .message("Authentication not authorized")
                                    .build()
                    );
        }

        if (exception instanceof SignatureException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(
                            DetailError.builder()
                                    .code(HttpStatus.FORBIDDEN.value())
                                    .date(LocalDateTime.now())
                                    .message("JWT not valid")
                                    .build()
                    );
        }

        if (exception instanceof TokenExpiredException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            DetailError.builder()
                                    .code(HttpStatus.UNAUTHORIZED.value())
                                    .date(LocalDateTime.now())
                                    .message("Token Expired")
                                    .build()
                    );
        }

        if (exception instanceof java.lang.SecurityException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(
                            DetailError.builder()
                                    .code(HttpStatus.FORBIDDEN.value())
                                    .date(LocalDateTime.now())
                                    .message("User not authenticated")
                                    .build()
                    );
        }


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        DetailError.builder()
                                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .date(LocalDateTime.now())
                                .message(exception.getMessage())
                                .build()
                );

    }

}
