package com.example.backend.service;

import com.example.backend.dao.UserConfirmationTokenDao;
import com.example.backend.entity.UserConfirmationTokenEntity;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class UserConfirmationTokenService {
    private final UserConfirmationTokenDao userConfirmationTokenDao;

    public UserConfirmationTokenService(@Qualifier("userConfirmationTokenJpa") UserConfirmationTokenDao userConfirmationTokenDao) {
        this.userConfirmationTokenDao = userConfirmationTokenDao;
    }

    public Optional<UserConfirmationTokenEntity> getTokenByUserId(String userId) {
        log.info("Return confirmation token for user with userId: {}", userId);

        return userConfirmationTokenDao.selectUserConfirmationTokenByUserId(userId);
    }

    public void saveUserConfirmationToken(UserConfirmationTokenEntity token) {
        log.info("Create confirmation token");

        userConfirmationTokenDao.insertUserConfirmationToken(token);
    }

    public Optional<UserConfirmationTokenEntity> getToken(String token) {
        log.info("Return confirmation token by token value");
        
        return userConfirmationTokenDao.selectUserConfirmationTokenByToken(token);
    }

    public int setConfirmedAt(String token) {
        log.info("Set confirmation date for confirmation token");

        return userConfirmationTokenDao.updateUserConfirmedAtByToken(
            token,
            LocalDateTime.now()
        );
    }

    public boolean existsConfirmationTokenByToken(String token) {
        return userConfirmationTokenDao.existsUserConfirmationTokenByToken(token);
    }

    public boolean existsConfirmationTokenByUserId(String userId) {
        return userConfirmationTokenDao.existsUserConfirmationTokenByUserId(userId);
    }
}
