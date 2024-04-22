package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.backend.dao.ProjectConfirmationTokenDao;
import com.example.backend.entity.ProjectConfirmationTokenEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectConfirmationTokenService {
    private final ProjectConfirmationTokenDao projectConfirmationTokenDao;

    public ProjectConfirmationTokenService(@Qualifier("projectConfirmationTokenJpa") ProjectConfirmationTokenDao projectConfirmationTokenDao) {
        this.projectConfirmationTokenDao = projectConfirmationTokenDao;
    }

    public Optional<ProjectConfirmationTokenEntity> getTokenByProjectKey(String projectKey) {
        log.info("Return confirmation token for project with projectKey: {}", projectKey);

        return projectConfirmationTokenDao.selectProjectConfirmationTokenByProjectKey(projectKey);
    }

    public void saveProjectConfirmationToken(ProjectConfirmationTokenEntity token) {
        log.info("Create confirmation token");

        projectConfirmationTokenDao.insertProjectConfirmationToken(token);
    }

    public Optional<ProjectConfirmationTokenEntity> getToken(String token) {
        log.info("Return confirmation token by token value");
        
        return projectConfirmationTokenDao.selectProjectConfirmationTokenByToken(token);
    }

    public int setConfirmedAt(String token) {
        log.info("Set confirmation date for confirmation token");

        return projectConfirmationTokenDao.updateProjectConfirmedAtByToken(
            token,
            LocalDateTime.now()
        );
    }

    public boolean existsConfirmationTokenByToken(String token) {
        return projectConfirmationTokenDao.existsProjectConfirmationTokenByToken(token);
    }

    public boolean existsConfirmationTokenByProjectKey(String projectKey) {
        return projectConfirmationTokenDao.existsProjectConfirmationTokenByProjectKey(projectKey);
    }
}
