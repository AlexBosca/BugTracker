package com.example.backend.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CredentialValidatorService {
    
    private final EmailValidatorService emailValidatorService;
    private final PasswordValidatorService passwordValidatorService;

    public boolean areCredentialsValid(String email, String password) {
        boolean isEmailValid = isEmailValid(email);
        boolean isPasswordValid = isPasswordValid(password);

        return isEmailValid && isPasswordValid;
    }

    public boolean isEmailValid(String email) {
        return emailValidatorService.test(email);
    }

    public boolean isPasswordValid(String password) {
        return passwordValidatorService.test(password);
    }
}
