package com.application.security.api.dto;

import com.application.security.domain.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserRequestDto {

    private String name;
    private String password;
    private String email;
    private boolean enable;
    @Singular(ignoreNullCollections = true)
    private List<String> roles;

    public User toEntity() {
        var user = new User();
        user.setPassword(password);
        user.setEmail(email);
        user.setName(name);
        return user;
    }
}
