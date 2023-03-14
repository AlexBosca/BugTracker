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
    public void deleteProjectByProjectId(String projectId) {
        projectRepository.deleteByProjectId(projectId);
    }

    @Override
    public boolean existsProjectWithProjectId(String projectId) {
        return projectRepository.existsByProjectId(projectId);
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
    public Optional<ProjectEntity> selectProjectById(String projectId) {
        return projectRepository.findByProjectId(projectId);
    }

    @Override
    public void updateProject(ProjectEntity project) {
        projectRepository.save(project);
    }
}
