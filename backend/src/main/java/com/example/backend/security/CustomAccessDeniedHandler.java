package com.example.backend.security;

import static com.example.backend.util.ExceptionUtilities.USER_ACCESS_DENIED;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.time.Clock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.backend.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Clock clock;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(
        HttpServletRequest request, 
        HttpServletResponse response, 
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResponse errorResponse = new ErrorResponse(
            clock,
            FORBIDDEN,
            USER_ACCESS_DENIED
        );

        response.setStatus(SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
    
}
