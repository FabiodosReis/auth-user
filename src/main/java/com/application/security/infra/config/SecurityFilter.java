package com.application.security.infra.config;

import com.application.security.domain.repository.UserRepository;
import com.application.security.infra.Service.JWTService;
import com.application.security.infra.exception.AuthenticationSecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
@Component
public class SecurityFilter extends OncePerRequestFilter {

    public SecurityFilter(
            JWTService tokenService,
            UserRepository userRepository,
            HandlerExceptionResolver exceptionResolver){

        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.exceptionResolver = exceptionResolver;

    }

    private final JWTService tokenService;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver exceptionResolver;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, AuthenticationSecurityException {

        if (request.getServletPath().equals("/api/v1/authentication/login") || request.getServletPath().equals("/api/v1/authentication/token/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {

                    String token = authorizationHeader.substring("Bearer ".length());
                    String userLogin = tokenService.validateAccessToken(token);
                    UserDetails principal = userRepository.findByLogin(userLogin);

                   /* List<String> roles = tokenService.extractRoles(token);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    // get principal from jwt token
                    Map<String, Object> principalMap = decodedJWT.getClaim("principal").asMap();
                    UserPrincipalRequestDto principal2 = new ObjectMapper().convertValue(principalMap, UserPrincipalRequestDto.class);*/


                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);

                    filterChain.doFilter(request, response);

                } catch (Exception e) {
                    exceptionResolver.resolveException(request, response , null, e);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
