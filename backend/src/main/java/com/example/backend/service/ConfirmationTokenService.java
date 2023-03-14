package com.example.backend.service;

import com.example.backend.dao.ConfirmationTokenDao;
import com.example.backend.entity.ConfirmationTokenEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    @Qualifier("confirmationToken-jpa")
    private final ConfirmationTokenDao confirmationTokenDao;

    public Optional<ConfirmationTokenEntity> getTokenByUserId(String userId) {
        log.info("Return confirmation token for user with userId: {}", userId);

        return confirmationTokenDao.selectConfirmationTokenByUserId(userId);
    }

    public void saveConfirmationToken(ConfirmationTokenEntity token) {
        log.info("Create confirmation token");

        confirmationTokenDao.insertConfirmationToken(token);
    }

    public Optional<ConfirmationTokenEntity> getToken(String token) {
        log.info("Return confirmation token by token value");
        
        return confirmationTokenDao.selectConfirmationTokenByToken(token);
    }

    public int setConfirmedAt(String token) {
        log.info("Set confirmation date for confirmation token");

        return confirmationTokenDao.updateConfirmedAtByToken(
            token,
            LocalDateTime.now()
        );
    }

    public boolean existsConfirmationTokenByToken(String token) {
        return confirmationTokenDao.existsConfirmationTokenByToken(token);
    }

    public boolean existsConfirmationTokenByUserId(String userId) {
        return confirmationTokenDao.existsConfirmationTokenByUserId(userId);
    }
}
