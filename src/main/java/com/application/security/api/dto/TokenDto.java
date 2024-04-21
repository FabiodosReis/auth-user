package com.application.security.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class TokenDto {
    private String token;
    private String refreshToken;
}
