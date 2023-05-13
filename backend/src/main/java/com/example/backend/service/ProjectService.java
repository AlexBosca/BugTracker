package com.example.backend.service;

import com.example.backend.dao.ProjectDao;
import com.example.backend.dao.TeamDao;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.TeamEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.project.ProjectAlreadyCreatedException;
import com.example.backend.exception.project.ProjectIdNotFoundException;
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
    
    private final TeamDao teamDao;

    public List<ProjectEntity> getAllProjects() {
        log.info(PROJECT_REQUEST_ALL);

        List<ProjectEntity> projects = projectDao.selectAllProjects();

        log.info(PROJECT_RETURN_ALL);

        return projects;
    }

    public ProjectEntity getProjectByProjectId(String projectId) {
        log.info(PROJECT_REQUEST_BY_ID, projectId);
        
        ProjectEntity project = projectDao
                .selectProjectById(projectId)
                .orElseThrow(() -> new ProjectIdNotFoundException(projectId));

        log.info(PROJECT_RETURN);

        return project;
    }

    public void saveProject(ProjectEntity project) {
        log.info(PROJECT_CREATE);

        boolean isProjectPresent = projectDao
                .existsProjectWithProjectId(project.getProjectId());

        if(isProjectPresent) {
            throw new ProjectAlreadyCreatedException(project.getProjectId());
        }

        projectDao.insertProject(project);

        log.info(PROJECT_CREATED);
    }

    public void addTeam(String projectId, String teamId) {
        log.info(PROJECT_ADD_TEAM_BY_ID, teamId, projectId);

        ProjectEntity project = projectDao
                .selectProjectById(projectId)
                .orElseThrow(() -> new ProjectIdNotFoundException(projectId));

        TeamEntity team = teamDao
                .selectTeamByTeamId(teamId)
                .orElseThrow(() -> new TeamIdNotFoundException(teamId));

        Set<TeamEntity> teams = project.getTeams();
        teams.add(team);
        project.setTeams(teams);

        projectDao.insertProject(project);

        log.info(PROJECT_TEAM_ADDED);
    }

    public List<IssueEntity> getAllIssuesOnProjectById(String projectId) {
        log.info("Request all issues on project with id: {}", projectId);

        ProjectEntity project = projectDao
                .selectProjectById(projectId)
                .orElseThrow(() -> new ProjectIdNotFoundException(projectId));

        List<IssueEntity> issuesOnProject = project.getIssues().stream()
                .collect(Collectors.toList());

        log.info("Return all issues on project with id: {}", projectId);

        return issuesOnProject;
    }
}
