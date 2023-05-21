package com.example.backend.dao;

import com.example.backend.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByProjectKey(String projectKey);

    boolean existsByProjectKey(String projectKey);

    @Modifying
    void deleteByProjectKey(String projectKey);

    // TODO: Implement an update method
}
