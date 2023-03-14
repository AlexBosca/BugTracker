package com.example.backend.dao;

import com.example.backend.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByProjectId(String projectId);

    boolean existsByProjectId(String projectId);

    @Modifying
    void deleteByProjectId(String projectId);

    // TODO: Implement an update method
}
