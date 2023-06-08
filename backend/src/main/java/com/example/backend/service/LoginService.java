package com.example.backend.service;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.dao.UserDao;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.BaseRuntimeException;
import com.example.backend.exception.user.UserCredentialsNotValidException;
import com.example.backend.exception.user.UserEmailNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class LoginService {
    
    private final UserDao userDao;
    
    private final PasswordEncoder passwordEncoder;

    public UsernamePasswordAuthenticationToken login(String email,  String password) {
        Optional<UserEntity> isUserPresent = userDao
                .selectUserByEmail(email);

        if(!isUserPresent.isPresent()) {
            throw new UserEmailNotFoundException(email);
        }

        UserEntity user = isUserPresent.get();

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserCredentialsNotValidException();
        }

        if(!user.isEnabled()) {
            // TODO: Handle in EntityExceptionHandler.java
            throw new BaseRuntimeException("Email address not confirmed");
        }

        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
