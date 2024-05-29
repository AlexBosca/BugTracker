package com.example.backend.service;

import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_ALL_RETRIEVED;
import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_CREATED;
import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_FILTERED_RETRIEVED;
import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_ISSUES_RETRIEVED;
import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_RETRIEVED;
import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_UPDATED;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.backend.dao.ProjectDao;
import com.example.backend.dao.UserDao;
import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.ProjectUpdateRequest;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.enums.UserRole;
import com.example.backend.exception.project.ProjectAlreadyCreatedException;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;
import com.example.backend.exception.user.UserUnexpectedRoleException;

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

    public void saveProject(ProjectEntity project, String projectManagerId) {
        boolean isProjectPresent = projectDao
            .existsProjectWithProjectKey(project.getProjectKey());

        UserEntity projectManager = userDao
            .selectUserByUserId(projectManagerId)
            .orElseThrow(() -> new UserIdNotFoundException(projectManagerId));

        boolean hasUserProjectManagerRole = projectManager
            .getRole()
            .equals(UserRole.ROLE_PROJECT_MANAGER);

        if(!hasUserProjectManagerRole) {
            throw new UserUnexpectedRoleException(projectManagerId, projectManager.getRole().getName(), UserRole.ROLE_PROJECT_MANAGER.getName());
        }


        if(isProjectPresent) {
            throw new ProjectAlreadyCreatedException(project.getProjectKey());
        }

        project.setProjectManager(projectManager);
        projectDao.insertProject(project);
        logInfo(PROJECT_CREATED, project);
    }

    public void updateProject(String projectKey, ProjectUpdateRequest request) {
        projectDao.updateProject(projectKey, request);
        logInfo(PROJECT_UPDATED, request);
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

    public void assignUsersOnProject(String projectKey,
                                     Set<String> usersIds) {
        ProjectEntity project = projectDao
            .selectProjectByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        Set<UserEntity> users = usersIds
            .stream()
            .sorted()
            .map(userId -> userDao
                .selectUserByUserId(userId)
                .orElseThrow(() -> new UserIdNotFoundException(userId)))
            .collect(Collectors.toSet());

        Set<UserEntity> assignedUsers = project.getAssignedUsers();
        assignedUsers.addAll(users);
        project.setAssignedUsers(assignedUsers);
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
