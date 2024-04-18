package com.example.backend.dao;

import com.example.backend.entity.ProjectConfirmationTokenEntity;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

public interface ProjectConfirmationTokenDao {
    List<ProjectConfirmationTokenEntity> selectAllProjectConfirmationTokens();
    Optional<ProjectConfirmationTokenEntity> selectProjectConfirmationTokenByUserId(String projectKey);
    Optional<ProjectConfirmationTokenEntity> selectProjectConfirmationTokenByToken(String token);
    void insertProjectConfirmationToken(ProjectConfirmationTokenEntity projectConfirmationToken);
    int updateProjectConfirmedAtByToken(String token, LocalDateTime confirmedAt);
    boolean existsProjectConfirmationTokenByProjectKey(String projectKey);
    boolean existsProjectConfirmationTokenByToken(String token);
}
