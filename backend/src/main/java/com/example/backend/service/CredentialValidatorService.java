package com.example.backend.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CredentialValidatorService {
    
    private final EmailValidatorService emailValidatorService;
    private final PasswordValidatorService passwordValidatorService;

    public boolean isValid(String email, String password) {
        boolean isEmailValid = emailValidatorService.test(email);
        boolean isPasswordValid = passwordValidatorService.test(password);

        return isEmailValid && isPasswordValid;
    }
}
