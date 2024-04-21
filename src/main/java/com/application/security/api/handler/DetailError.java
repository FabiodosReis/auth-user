package com.application.security.api.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter @Setter
@Builder
public class DetailError {
    private int code;
    private LocalDateTime date;
    private String message;
}
