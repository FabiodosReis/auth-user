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
        var user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setName(name);
        return user;
    }
}
