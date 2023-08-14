package com.example.backend.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.backend.entity.UserEntity;
import com.example.backend.exception.BaseRuntimeException;
import com.example.backend.exception.registration.EmailNotConfirmedException;
import com.example.backend.exception.user.UserCredentialsExpiredException;
import com.example.backend.exception.user.UserCredentialsNotValidException;
import com.example.backend.service.AppUserDetailsService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppAuthProvider implements AuthenticationProvider {

    private final AppUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication == null) {
            throw new BaseRuntimeException("Authentication failed");
        }

        String email = String.valueOf(authentication.getName());
        String password = String.valueOf(authentication.getCredentials());

        UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(email);

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserCredentialsNotValidException();
        }

        if(!user.isEnabled() &&
           !user.isAccountNonExpired() &&
           !user.isAccountNonLocked() &&
           !user.isCredentialsNonExpired()) {
            throw new EmailNotConfirmedException();
        }

        if(!user.isEnabled()) {
            throw new BaseRuntimeException("Account disabled");
        }

        if(!user.isCredentialsNonExpired()) {
            throw new UserCredentialsExpiredException();
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
    }
    
}
