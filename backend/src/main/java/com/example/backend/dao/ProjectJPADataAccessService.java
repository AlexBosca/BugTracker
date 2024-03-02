package com.example.backend.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.filter.FilterUtility;
import com.example.backend.entity.ProjectEntity;

@Repository("projectJpa")
public class ProjectJPADataAccessService implements ProjectDao {

    private final FilterUtility<ProjectEntity> filterUtility;
    private final ProjectRepository projectRepository;

    public ProjectJPADataAccessService(EntityManager entityManager, ProjectRepository projectRepository) {
        this.filterUtility = new FilterUtility<>(entityManager, ProjectEntity.class);
        this.projectRepository = projectRepository;
    }

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
    public List<ProjectEntity> selectAllFilteredProjects(FilterCriteria filterCriteria) {
        return filterUtility.filterEntities(filterCriteria);
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
