package com.example.backend.security;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backend.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountLockingFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String basicAuth = request.getHeader("Authorization");
        String email = authenticationService.getEmailFromAuthorizationHeader(basicAuth);
        boolean isAccountLocked = authenticationService.isAccountLockedByEmail(email);

        if(isAccountLocked) {
            response.setStatus(FORBIDDEN.value());
            return;
        }

        filterChain.doFilter(request, response);
    }
    
}
