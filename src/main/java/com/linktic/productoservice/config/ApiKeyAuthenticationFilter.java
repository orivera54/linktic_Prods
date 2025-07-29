package com.linktic.productoservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final String apiKey;

    public ApiKeyAuthenticationFilter(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestApiKey = request.getHeader("X-API-Key");
        
        if (apiKey.equals(requestApiKey)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "service-user",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_SERVICE"))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
}