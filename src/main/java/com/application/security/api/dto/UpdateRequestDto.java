package com.application.security.api.dto;

import com.application.security.domain.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRequestDto {

    private String name;
    private String email;
    private String confirmEmail;

    public User toEntity(String id) {
        return User.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();

    }

}
