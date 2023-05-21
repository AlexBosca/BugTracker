package com.example.backend.dao;

import com.example.backend.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

public interface ProjectDao {
    List<ProjectEntity> selectAllProjects();
    Optional<ProjectEntity> selectProjectByKey(String projectKey);
    void insertProject(ProjectEntity project);
    boolean existsProjectWithProjectKey(String projectKey);
    void deleteProjectByProjectKey(String projectKey);
    void updateProject(ProjectEntity project);
}
