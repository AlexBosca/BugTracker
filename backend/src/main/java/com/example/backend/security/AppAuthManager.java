package com.example.backend.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppAuthManager implements AuthenticationManager {
    private final AuthenticationProvider authProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authProvider.authenticate(authentication);
    }
    
}
