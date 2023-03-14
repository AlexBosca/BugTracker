package com.example.backend.dao;

import com.example.backend.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

public interface ProjectDao {
    List<ProjectEntity> selectAllProjects();
    Optional<ProjectEntity> selectProjectById(String projectId);
    void insertProject(ProjectEntity project);
    boolean existsProjectWithProjectId(String projectId);
    void deleteProjectByProjectId(String projectId);
    void updateProject(ProjectEntity project);
}
