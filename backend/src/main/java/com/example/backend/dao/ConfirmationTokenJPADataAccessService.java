package com.example.backend.dao;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.backend.entity.ConfirmationTokenEntity;

import lombok.RequiredArgsConstructor;

@Repository("confirmationTokenJpa")
@RequiredArgsConstructor
public class ConfirmationTokenJPADataAccessService implements ConfirmationTokenDao {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void insertConfirmationToken(ConfirmationTokenEntity confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
        
    }

    @Override
    public List<ConfirmationTokenEntity> selectAllConfirmationTokens() {
        return confirmationTokenRepository.findAll();
    }

    @Override
    public Optional<ConfirmationTokenEntity> selectConfirmationTokenByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    public Optional<ConfirmationTokenEntity> selectConfirmationTokenByUserId(String userId) {
        return confirmationTokenRepository.findByUserId(userId);
    }

    @Override
    public int updateConfirmedAtByToken(String token, LocalDateTime confirmedAt) {
        return confirmationTokenRepository.updateConfirmedAt(token, confirmedAt);
    }

    @Override
    public boolean existsConfirmationTokenByToken(String token) {
        return confirmationTokenRepository.existsByToken(token);
    }

    @Override
    public boolean existsConfirmationTokenByUserId(String userId) {
        return confirmationTokenRepository.existsByUserId(userId);

    }

    
}
