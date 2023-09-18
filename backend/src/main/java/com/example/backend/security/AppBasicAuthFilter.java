package com.example.backend.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backend.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

import static com.example.backend.util.Utilities.AUTHENTICATION_REQUEST_PATH;

@Component
@RequiredArgsConstructor
public class AppBasicAuthFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken authenticationToken = authenticationService.getAuthenticationToken(authHeader);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        return path.startsWith(AUTHENTICATION_REQUEST_PATH) && (method.equals("GET") || method.equals("POST"));
    }
}
