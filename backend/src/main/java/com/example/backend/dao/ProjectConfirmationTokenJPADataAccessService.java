package com.example.backend.dao;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.backend.entity.ProjectConfirmationTokenEntity;

import lombok.RequiredArgsConstructor;

@Repository("projectConfirmationTokenJpa")
@RequiredArgsConstructor
public class ProjectConfirmationTokenJPADataAccessService implements ProjectConfirmationTokenDao {

    private final ProjectConfirmationTokenRepository projectConfirmationTokenRepository;

    @Override
    public void insertProjectConfirmationToken(ProjectConfirmationTokenEntity projectConfirmationToken) {
        projectConfirmationTokenRepository.save(projectConfirmationToken);
        
    }

    @Override
    public List<ProjectConfirmationTokenEntity> selectAllProjectConfirmationTokens() {
        return projectConfirmationTokenRepository.findAll();
    }

    @Override
    public Optional<ProjectConfirmationTokenEntity> selectProjectConfirmationTokenByToken(String token) {
        return projectConfirmationTokenRepository.findByToken(token);
    }

    @Override
    public Optional<ProjectConfirmationTokenEntity> selectProjectConfirmationTokenByUserId(String userId) {
        return projectConfirmationTokenRepository.findByProjectKey(userId);
    }

    @Override
    public int updateProjectConfirmedAtByToken(String token, LocalDateTime confirmedAt) {
        return projectConfirmationTokenRepository.updateConfirmedAt(token, confirmedAt);
    }

    @Override
    public boolean existsProjectConfirmationTokenByToken(String token) {
        return projectConfirmationTokenRepository.existsByToken(token);
    }

    @Override
    public boolean existsProjectConfirmationTokenByProjectKey(String projectKey) {
        return projectConfirmationTokenRepository.existsByProjectKey(projectKey);

    }

    
}
