package com.example.backend.dao;

import com.example.backend.entity.UserConfirmationTokenEntity;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

public interface UserConfirmationTokenDao {
    List<UserConfirmationTokenEntity> selectAllProjectConfirmationTokens();
    Optional<UserConfirmationTokenEntity> selectUserConfirmationTokenByUserId(String userId);
    Optional<UserConfirmationTokenEntity> selectUserConfirmationTokenByToken(String token);
    void insertUserConfirmationToken(UserConfirmationTokenEntity userConfirmationToken);
    int updateUserConfirmedAtByToken(String token, LocalDateTime confirmedAt);
    boolean existsUserConfirmationTokenByUserId(String userId);
    boolean existsUserConfirmationTokenByToken(String token);
}
