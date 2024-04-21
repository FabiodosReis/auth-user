package com.application.security.api.controller;

import com.application.security.api.dto.TokenDto;
import com.application.security.api.dto.UserPrincipalRequestDto;
import com.application.security.domain.model.User;
import com.application.security.infra.Service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;

    private final JWTService tokenService;

    @PostMapping("login")
    public ResponseEntity<TokenDto> login(@RequestBody UserPrincipalRequestDto auth) {

        UsernamePasswordAuthenticationToken user =
                new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword());

        Authentication authorization = authManager.authenticate(user);

        TokenDto tokenResponse = tokenService.generateToken(
                ((User) authorization.getPrincipal()).getEmail(),
                authorization.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("token/refresh")
    public ResponseEntity<TokenDto> tokenRefresh(@RequestBody TokenDto dto) {
        return ResponseEntity.ok(tokenService.generateTokenByRefreshToken(dto));
    }

}
