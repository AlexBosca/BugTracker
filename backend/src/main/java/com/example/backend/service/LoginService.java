package com.example.backend.service;

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

    public UserEntity login(String email,  String password) {
        boolean isUserPresent = userDao.existsUserByEmail(email);

        if(!isUserPresent) {
            throw new UserEmailNotFoundException(email);
        }

        UserEntity user = userDao.selectUserByEmail(email).get();

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserCredentialsNotValidException();
        }

        if(!user.isEnabled()) {
            // TODO: Handle in EntityExceptionHandler.java
            throw new BaseRuntimeException("Email address not confirmed");
        }

        return user;
    }
}
