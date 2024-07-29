package com.application.security.api.dto;

import com.application.security.domain.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestDto {

    private String name;
    private String password;
    private String email;
    private boolean enable;

    private List<String> roles = new ArrayList<>();

    public User toEntity() {
        return User.builder()
                .password(password)
                .email(email)
                .name(name)
                .build();

    }
}
