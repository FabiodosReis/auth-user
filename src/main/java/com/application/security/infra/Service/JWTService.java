package com.application.security.infra.Service;

import com.application.security.api.dto.TokenDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static com.application.security.infra.constants.ClaimConstants.*;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Service
@Log4j2
public class JWTService {

    @Value("${api.security.token.secret}")
    private String tokenSecret;

    @Value("${api.security.refresh.token.secret}")
    private String refreshTokenSecret;

    @Value("${spring.application.name}")
    private String applicationName;

    public TokenDto generateToken(String subject, List<String> authorities) throws SecurityException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
            String token = JWT.create()
                    .withIssuer(applicationName)
                    .withSubject(subject)
                    .withClaim(ROLES, authorities)
                    .withExpiresAt(generateExpirationTokenDate())
                    .sign(algorithm);

            String refreshToken = generateRefreshToken(token);

            log.info("Login successfully");

            return TokenDto.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();

        } catch (Exception e) {
            throw new SecurityException("Error while generating token", e);
        }
    }

    public TokenDto generateTokenByRefreshToken(TokenDto dto) throws SecurityException {
        try {
            String subject = validateRefreshToken(dto.getRefreshToken());
            List<String> roles = extractRoles(dto.getToken());
            return this.generateToken(subject, roles);
        } catch (Exception e) {
            throw e;
        }
    }
    private String generateRefreshToken(String token) throws SecurityException {
        try {
            if (isTokenValid(token)) {
                Algorithm algorithm = Algorithm.HMAC256(refreshTokenSecret);
                Instant tokenExpireDate = extractExpiration(token);
                Instant refreshTokenExpireDate = tokenExpireDate
                        .plus(1, ChronoUnit.HOURS)
                        .atZone(ZoneOffset.UTC).toInstant();

                return JWT.create()
                        .withIssuer(applicationName)
                        .withSubject(extractSubject(token))
                        .withExpiresAt(refreshTokenExpireDate)
                        .sign(algorithm);
            } else {
                log.error("Invalid Token");
                throw new BadCredentialsException("Invalid Token");
            }
        } catch (Exception e) {
            log.error("Error while generating refresh token");
            throw new SecurityException("Error while generating refresh token", e);
        }
    }

    private boolean isTokenValid(String token) {
        final String username = validateAccessToken(token);
        return isNotEmpty(username);
    }

    public String validateAccessToken(String token) {
        try {
            return validateToken(token, tokenSecret);
        } catch (JWTVerificationException e) {
            throw e;
        }
    }

    private String validateRefreshToken(String refreshToken) {
        try {
            return validateToken(refreshToken, refreshTokenSecret);
        } catch (JWTVerificationException e) {
            throw e;
        }
    }

    private String validateToken(String token, String secret) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(applicationName)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (SignatureVerificationException | TokenExpiredException | AccessDeniedException |
                 BadCredentialsException e) {
            throw e;
        } catch (SecurityException e) {
            log.error("Error try authentication user");
            throw new SecurityException("Error try authentication user");
        }
    }

    private Instant generateExpirationTokenDate() {
        return LocalDateTime.now().plusHours(1)
                .toInstant(ZoneOffset.UTC);
    }

    private Instant extractExpiration(String token) {
        return getClaims(token)
                .get(EXPIRATION)
                .asDate()
                .toInstant().atZone(ZoneOffset.UTC).toInstant();
    }

    private String extractSubject(String token) {
        return getClaims(token)
                .get(SUBJECT)
                .asString();
    }

    public List<String> extractRoles(String token) {
        return getClaims(token)
                .get(ROLES)
                .asList(String.class);
    }

    private Map<String, Claim> getClaims(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(tokenSecret))
                    .withIssuer(applicationName)
                    .build()
                    .verify(token)
                    .getClaims();
        } catch (JWTVerificationException e) {
            throw e;
        }
    }
}