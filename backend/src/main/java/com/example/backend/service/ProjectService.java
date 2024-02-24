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

import static com.example.backend.util.project.ProjectUtilities.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProjectService {

    @Qualifier("project-jpa")
    private final ProjectDao projectDao;
    
    @Qualifier("team-jpa")
    private final TeamDao teamDao;

    public List<ProjectEntity> getAllProjects() {
        log.info(PROJECT_REQUEST_ALL);

        List<ProjectEntity> projects = projectDao.selectAllProjects();

        log.info(PROJECT_RETURN_ALL);

        return projects;
    }

    public List<ProjectEntity> filterProjects(FilterCriteria filterCriteria) {
        return projectDao.selectAllFilteredProjects(filterCriteria);
    }

    public ProjectEntity getProjectByProjectKey(String projectKey) {
        log.info(PROJECT_REQUEST_BY_ID, projectKey);
        
        ProjectEntity project = projectDao
            .selectProjectByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        log.info(PROJECT_RETURN);

        return project;
    }

    public void saveProject(ProjectEntity project) {
        log.info(PROJECT_CREATE);

        boolean isProjectPresent = projectDao
            .existsProjectWithProjectKey(project.getProjectKey());

        if(isProjectPresent) {
            throw new ProjectAlreadyCreatedException(project.getProjectKey());
        }

        projectDao.insertProject(project);

        log.info(PROJECT_CREATED);
    }

    public void addTeam(String projectKey, String teamId) {
        log.info(PROJECT_ADD_TEAM_BY_ID, teamId, projectKey);

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

        log.info(PROJECT_TEAM_ADDED);
    }

    public List<IssueEntity> getAllIssuesOnProjectById(String projectKey) {
        log.info(PROJECT_REQUEST_ALL_ISSUES_BY_ID, projectKey);

        ProjectEntity project = projectDao
            .selectProjectByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        List<IssueEntity> issuesOnProject = project.getIssues().stream()
            .collect(Collectors.toList());

        log.info(PROJECT_RETURN_ALL_ISSUES_BY_ID, projectKey);

        return issuesOnProject;
    }

    public List<TeamEntity> getAllTeamsOnProjectById(String projectKey) {
        log.info(PROJECT_REQUEST_ALL_TEAMS_BY_ID, projectKey);

        ProjectEntity project = projectDao
            .selectProjectByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        List<TeamEntity> teamsOnProject = project.getTeams().stream()
            .collect(Collectors.toList());

        log.info(PROJECT_RETURN_ALL_TEAMS_BY_ID, projectKey);

        return teamsOnProject;
    }
}
