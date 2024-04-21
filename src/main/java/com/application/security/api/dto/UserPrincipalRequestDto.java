package com.application.security.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserPrincipalRequestDto {

    public UserPrincipalRequestDto(String id, String email){
        this.id = id;
        this.email = email;
    }

    public UserPrincipalRequestDto essence(){
        return new UserPrincipalRequestDto(id, email);
    }

    private String id;
    private String email;
    private String password;
}
