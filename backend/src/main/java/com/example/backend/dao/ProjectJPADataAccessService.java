package com.example.backend.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.filter.FilterUtility;
import com.example.backend.dto.request.ProjectUpdateRequest;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.exception.DataAccessServiceException;

import lombok.extern.slf4j.Slf4j;

import static com.example.backend.util.database.DatabaseLoggingMessages.*;

@Slf4j
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
        try {
            projectRepository.deleteByProjectKey(projectKey);
            log.info(ENTITY_DELETED, projectKey);
        } catch (DataAccessException e) {
            log.error(ENTITY_DELETE_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public boolean existsProjectWithProjectKey(String projectKey) {
        try {
            boolean exists = projectRepository.existsByProjectKey(projectKey);
            
            if(exists) {
                log.info(ENTITY_EXISTS, projectKey);
            } else {
                log.info(ENTITY_NOT_EXISTS, projectKey);
            }

            return exists;
        } catch (DataAccessException e) {
            log.error(ENTITY_EXISTS_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public void insertProject(ProjectEntity project) {
        try {
            projectRepository.save(project);
            log.info(ENTITY_SAVED);
        } catch (DataAccessException e) {
            log.error(ENTITY_SAVE_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public List<ProjectEntity> selectAllProjects() {
        try {
            List<ProjectEntity> projects = projectRepository.findAll();
            log.info(ENTITIES_FETCHED, projects);

            return projects;
        } catch (DataAccessException e) {
            log.error(ENTITY_FETCH_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public List<ProjectEntity> selectAllFilteredProjects(FilterCriteria filterCriteria) {
        List<ProjectEntity> filterProjects = filterUtility.filterEntities(filterCriteria);
        log.info(ENTITIES_FILTERED_FETCHED, filterCriteria);

        return filterProjects;
    }

    @Override
    public Optional<ProjectEntity> selectProjectByKey(String projectKey) {
        try {
            Optional<ProjectEntity> project = projectRepository.findByProjectKey(projectKey);

            if(project.isPresent()) {
                log.info(ENTITY_FETCHED, projectKey);
            } else {
                log.warn(ENTITY_NOT_FOUND, projectKey);
            }

            return project;
        } catch (DataAccessException e) {
            log.error(ENTITY_FETCH_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public void updateProject(String projectKey, ProjectUpdateRequest request) {
        try {
            if(request.getProjectKey() != null) {
                projectRepository.updateProjectKey(projectKey, request.getProjectKey());
            }

            if(request.getName() != null) {
                projectRepository.updateProjectName(projectKey, request.getName());
            }

            if(request.getDescription() != null) {
                projectRepository.updateProjectDescription(projectKey, request.getDescription());
            }

            if(request.getStartDate() != null) {
                projectRepository.updateProjectStartDate(projectKey, request.getStartDate());
            }

            if(request.getTargetEndDate() != null) {
                projectRepository.updateProjectTargetEndDate(projectKey, request.getTargetEndDate());
            }

            if(request.getActualEndDate() != null) {
                projectRepository.updateProjectActualEndDate(projectKey, request.getActualEndDate());
            }

            if(request.getProjectManagerId() != null) {
                projectRepository.updateProjectManagerId(projectKey, request.getProjectManagerId());
            }

            log.info(ENTITY_UPDATED);
        } catch (DataAccessException e) {
            log.error(ENTITY_UPDATE_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }
}
