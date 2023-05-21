package com.example.backend.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.backend.entity.ProjectEntity;

import lombok.RequiredArgsConstructor;

@Repository("project-jpa")
@RequiredArgsConstructor
public class ProjectJPADataAccessService implements ProjectDao {

    private final ProjectRepository projectRepository;

    @Override
    public void deleteProjectByProjectKey(String projectKey) {
        projectRepository.deleteByProjectKey(projectKey);
    }

    @Override
    public boolean existsProjectWithProjectKey(String projectKey) {
        return projectRepository.existsByProjectKey(projectKey);
    }

    @Override
    public void insertProject(ProjectEntity project) {
        projectRepository.save(project);
    }

    @Override
    public List<ProjectEntity> selectAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<ProjectEntity> selectProjectByKey(String projectKey) {
        return projectRepository.findByProjectKey(projectKey);
    }

    @Override
    public void updateProject(ProjectEntity project) {
        projectRepository.save(project);
    }
}
