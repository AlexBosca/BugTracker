package com.example.backend.service;

import com.example.backend.dao.ProjectDao;
import com.example.backend.dao.TeamDao;
import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.TeamEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.project.ProjectAlreadyCreatedException;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.exception.team.TeamIdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.example.backend.util.issue.IssueLoggingMessages.ISSUE_RETRIEVED;
import static com.example.backend.util.project.ProjectLoggingMessages.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectService {
    private final ProjectDao projectDao;
    private final TeamDao teamDao;

    public ProjectService(@Qualifier("projectJpa") ProjectDao projectDao,
                          @Qualifier("teamJpa") TeamDao teamDao) {
        
        this.projectDao = projectDao;
        this.teamDao = teamDao;
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

    public void addTeam(String projectKey, String teamId) {
        ProjectEntity project = projectDao
            .selectProjectByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        TeamEntity team = teamDao
            .selectTeamByTeamId(teamId)
            .orElseThrow(() -> new TeamIdNotFoundException(teamId));

        project.addTeam(team);
        team.addProject(project);

        projectDao.insertProject(project);
        teamDao.insertTeam(team);
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

    public List<TeamEntity> getAllTeamsOnProjectById(String projectKey) {
        ProjectEntity project = projectDao
            .selectProjectByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        List<TeamEntity> teamsOnProject = project.getTeams().stream()
            .collect(Collectors.toList());

        return teamsOnProject;
    }

    private void logInfo(String var1, Object var2) {
        log.info(var1, "Service", var2);
    }
}
