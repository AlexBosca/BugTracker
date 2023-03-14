package com.example.backend.dao;

import com.example.backend.entity.ConfirmationTokenEntity;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

public interface ConfirmationTokenDao {
    List<ConfirmationTokenEntity> selectAllConfirmationTokens();
    Optional<ConfirmationTokenEntity> selectConfirmationTokenByUserId(String userId);
    Optional<ConfirmationTokenEntity> selectConfirmationTokenByToken(String token);
    void insertConfirmationToken(ConfirmationTokenEntity confirmationToken);
    int updateConfirmedAtByToken(String token, LocalDateTime confirmedAt);
    boolean existsConfirmationTokenByUserId(String userId);
    boolean existsConfirmationTokenByToken(String token);
}
