package com.example.backend.service;

import com.example.backend.dao.ProjectRepository;
import com.example.backend.dao.TeamRepository;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.TeamEntity;
import com.example.backend.exception.project.ProjectIdNotFoundException;
import com.example.backend.exception.team.TeamIdNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectService {

//    private final static String PROJECT_NOT_FOUND_MESSAGE = "Project with id %s not found";
//    private final static String TEAM_NOT_FOUND_MESSAGE = "Team with id %s not found";


    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;

    public ProjectEntity getProjectByProjectId(String projectId) {
        return projectRepository
                .findByProjectId(projectId)
                .orElseThrow(() -> new ProjectIdNotFoundException(projectId));
    }

    public ProjectEntity saveProject(ProjectEntity project) {
        return projectRepository.save(project);
    }

    public void addTeam(String projectId, String teamId) {
        ProjectEntity project = projectRepository
                .findByProjectId(projectId)
                .orElseThrow(() -> new ProjectIdNotFoundException(projectId));

        TeamEntity team = teamRepository
                .findByTeamId(teamId)
                .orElseThrow(() -> new TeamIdNotFoundException(teamId));

        project.getTeams().add(team);

        projectRepository.save(project);
    }
}
