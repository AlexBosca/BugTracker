package com.example.backend.service;

import com.example.backend.dto.request.RegistrationRequest;
import com.example.backend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

//TODO: Add logging wherever it is neccessary
@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService {

    @Autowired
    private final RegistrationService registrationService;
    @Autowired
    private final LoginService loginService;

    public String register(RegistrationRequest request) {
        return registrationService.register(request);
    }

    public String confirmToken(String token) {
        return registrationService.confirmToken(token);
    }

    public UserEntity login(String authorizationHeader) {
        Credentials credentials = this.extractCredentials(authorizationHeader);

        return loginService.login(credentials.getEmail(), credentials.getPassword());
    }

    public String getEmailFromAuthorizationHeader(String authorizsationHeader) {
        return this.extractCredentials(authorizsationHeader).getEmail();
    }

    private Credentials extractCredentials(String authorizationHeader) {
        String encodedCredentials = authorizationHeader.split(" ")[1];
        String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials));
        String[] splitCredentials = decodedCredentials.split(":");

        return new Credentials(splitCredentials[0], splitCredentials[1]);
    }

    @Data
    @AllArgsConstructor
    private final class Credentials {
        private final String email;
        private final String password;
    }
}
