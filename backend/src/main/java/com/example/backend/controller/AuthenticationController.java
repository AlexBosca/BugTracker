package com.example.backend.controller;

import com.example.backend.entity.UserEntity;
import com.example.backend.dto.request.RegistrationRequest;
import com.example.backend.dto.request.ResetPasswordRequest;
import com.example.backend.dto.response.UserFullResponse;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final MapStructMapper mapper;
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        String responseBody = authenticationService.register(mapper.toEntity(request));

        return new ResponseEntity<>(responseBody, CREATED);
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        String responseBody = authenticationService.confirmToken(token);

        return new ResponseEntity<>(responseBody, OK);
    }

    @GetMapping
    public ResponseEntity<UserFullResponse> login(@RequestHeader("Authorization") String authorizationHeader) {
        Authentication authentication = authenticationManager
                .authenticate(authenticationService.getAuthenticationToken(authorizationHeader));


        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = (UserEntity) authentication.getPrincipal();

        UserFullResponse userResponse = mapper
                .toResponse(user);
        
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping(path = "/resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        authenticationService.resetPassword(request);

        return new ResponseEntity<>(OK);
    }
}
