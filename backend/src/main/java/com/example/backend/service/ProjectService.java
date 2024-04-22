package com.example.backend.service;

import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_ALL_RETRIEVED;
import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_CREATED;
import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_FILTERED_RETRIEVED;
import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_ISSUES_RETRIEVED;
import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_RETRIEVED;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.backend.dao.ProjectDao;
import com.example.backend.dao.UserDao;
import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.project.ProjectAlreadyCreatedException;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectService {
    private final ProjectDao projectDao;
    private final UserDao userDao;

    public ProjectService(@Qualifier("projectJpa") ProjectDao projectDao,
                          @Qualifier("userJpa") UserDao userDao) {
        this.projectDao = projectDao;
        this.userDao = userDao;
    }

    public List<ProjectEntity> getAllProjects() {
        List<ProjectEntity> projects = projectDao.selectAllProjects();
        logInfo(PROJECT_ALL_RETRIEVED, projects);

        return projects;
    }

    public List<ProjectEntity> filterProjects(FilterCriteria filterCriteria) {
        List<ProjectEntity> projects = projectDao.selectAllFilteredProjects(filterCriteria);
        logInfo(PROJECT_FILTERED_RETRIEVED, projects);

        return projects;
    }

    public ProjectEntity getProjectByProjectKey(String projectKey) {
        ProjectEntity project = projectDao
            .selectProjectByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        logInfo(PROJECT_RETRIEVED, project);

        return project;
    }

    public void saveProject(ProjectEntity project) {
        boolean isProjectPresent = projectDao
            .existsProjectWithProjectKey(project.getProjectKey());

        if(isProjectPresent) {
            throw new ProjectAlreadyCreatedException(project.getProjectKey());
        }

        projectDao.insertProject(project);
        logInfo(PROJECT_CREATED, project);
    }

    public void assignUserOnProject(String projectKey,
                                    String userId) {
        ProjectEntity project = projectDao
            .selectProjectByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));
        
        UserEntity user = userDao
            .selectUserByUserId(userId)
            .orElseThrow(() -> new UserIdNotFoundException(userId));

        project.getAssignedUsers().add(user);
        projectDao.insertProject(project);
    }

    public List<IssueEntity> getAllIssuesOnProjectById(String projectKey) {
        ProjectEntity project = projectDao
            .selectProjectByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        List<IssueEntity> issuesOnProject = project.getIssues().stream()
            .collect(Collectors.toList());

        logInfo(PROJECT_ISSUES_RETRIEVED, issuesOnProject);

        return issuesOnProject;
    }

    private void logInfo(String var1, Object var2) {
        log.info(var1, "Service", var2);
    }
}
