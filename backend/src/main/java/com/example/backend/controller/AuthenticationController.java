package com.example.backend.controller;

import com.example.backend.entity.UserEntity;
import com.example.backend.dto.request.RegistrationRequest;
import com.example.backend.dto.response.UserFullResponse;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.AuthenticationService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "authentication")
@AllArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final MapStructMapper mapper;
    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping
    public void register(@RequestBody RegistrationRequest request) {
        authenticationService.register(request);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return authenticationService.confirmToken(token);
    }

    @GetMapping
    public ResponseEntity<UserFullResponse> login(@RequestHeader("Authorization") String authorizationHeader) {
        Authentication authentication = authenticationManager
                .authenticate(authenticationService.login(authorizationHeader));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = (UserEntity) authentication.getPrincipal();

        UserFullResponse userResponse = mapper
                .toResponse(user);
        
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
