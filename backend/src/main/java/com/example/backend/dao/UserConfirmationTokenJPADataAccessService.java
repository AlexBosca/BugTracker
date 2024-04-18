package com.example.backend.dao;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.backend.entity.UserConfirmationTokenEntity;

import lombok.RequiredArgsConstructor;

@Repository("userConfirmationTokenJpa")
@RequiredArgsConstructor
public class UserConfirmationTokenJPADataAccessService implements UserConfirmationTokenDao {

    private final UserConfirmationTokenRepository userConfirmationTokenRepository;

    @Override
    public void insertUserConfirmationToken(UserConfirmationTokenEntity userConfirmationToken) {
        userConfirmationTokenRepository.save(userConfirmationToken);
        
    }

    @Override
    public List<UserConfirmationTokenEntity> selectAllProjectConfirmationTokens() {
        return userConfirmationTokenRepository.findAll();
    }

    @Override
    public Optional<UserConfirmationTokenEntity> selectUserConfirmationTokenByToken(String token) {
        return userConfirmationTokenRepository.findByToken(token);
    }

    @Override
    public Optional<UserConfirmationTokenEntity> selectUserConfirmationTokenByUserId(String userId) {
        return userConfirmationTokenRepository.findByUserId(userId);
    }

    @Override
    public int updateUserConfirmedAtByToken(String token, LocalDateTime confirmedAt) {
        return userConfirmationTokenRepository.updateConfirmedAt(token, confirmedAt);
    }

    @Override
    public boolean existsUserConfirmationTokenByToken(String token) {
        return userConfirmationTokenRepository.existsByToken(token);
    }

    @Override
    public boolean existsUserConfirmationTokenByUserId(String userId) {
        return userConfirmationTokenRepository.existsByUserId(userId);

    }

    
}
