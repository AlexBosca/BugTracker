package com.example.backend.dao;

import com.example.backend.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByProjectKey(String projectKey);

    boolean existsByProjectKey(String projectKey);

    @Modifying
    void deleteByProjectKey(String projectKey);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectEntity project " +
            "SET project.projectKey = ?2 WHERE project.projectKey = ?1")
    void updateProjectKey(String existingProjectKey, String newProjectKey);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectEntity project " +
            "SET project.name = ?2 WHERE project.projectKey = ?1")
    void updateProjectName(String projectKey, String name);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectEntity project " +
            "SET project.description = ?2 WHERE project.projectKey = ?1")
    void updateProjectDescription(String projectKey, String description);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectEntity project " +
            "SET project.startDate = ?2 WHERE project.projectKey = ?1")
    void updateProjectStartDate(String projectKey, LocalDateTime startDate);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectEntity project " +
            "SET project.targetEndDate = ?2 WHERE project.projectKey = ?1")
    void updateProjectTargetEndDate(String projectKey, LocalDateTime targetEndDate);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectEntity project " +
            "SET project.actualEndDate = ?2 WHERE project.projectKey = ?1")
    void updateProjectActualEndDate(String projectKey, LocalDateTime actualEndDate);

    @Modifying
    @Transactional
    @Query("UPDATE ProjectEntity project " +
            "SET project.projectManager.id = (SELECT user.id FROM UserEntity user WHERE user.userId = ?2) " +
            "WHERE project.projectKey = ?1")
    void updateProjectManagerId(String projectKey, String projectManagerId);
}
