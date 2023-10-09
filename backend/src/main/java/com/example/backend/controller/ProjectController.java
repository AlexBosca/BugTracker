package com.example.backend.controller;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.ProjectRequest;
import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.dto.response.ProjectFullResponse;
import com.example.backend.dto.response.TeamFullResponse;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.TeamEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.backend.util.project.ProjectUtilities.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final MapStructMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProjectFullResponse>> getAllProjects() {
        log.info(PROJECT_GET_ALL);

        List<ProjectEntity> entities = projectService.getAllProjects();

        List<ProjectFullResponse> responses = entities.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());

        return new ResponseEntity<>(
            responses,
            OK
        );
    }

    @PostMapping(path = "/filter")
    public ResponseEntity<List<ProjectFullResponse>> getFilteredProjects(@RequestBody FilterCriteria filterCriteria) {
        List<ProjectEntity> entities = projectService.filterProjects(filterCriteria);

        List<ProjectFullResponse> responses = entities.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());

        return new ResponseEntity<>(
            responses,
            OK
        );
    }

    @GetMapping(path = "/{projectKey}")
    public ResponseEntity<ProjectFullResponse> getProject(@PathVariable(name = "projectKey") String projectKey) {
        log.info(PROJECT_GET_BY_ID, projectKey);
        
        return new ResponseEntity<>(
            mapper.toResponse(projectService.getProjectByProjectKey(projectKey)),
            OK
        );
    }

    @PostMapping
    public ResponseEntity<Void> createProject(@RequestBody ProjectRequest request) {
        log.info(PROJECT_CREATE_NEW);
        
        projectService.saveProject(mapper.toEntity(request));

        return new ResponseEntity<>(CREATED);
    }

    @PutMapping(path = "/{projectKey}/addTeam/{teamId}")
    public ResponseEntity<Void> addTeamToProject(@PathVariable(name = "projectKey") String projectKey, @PathVariable(name = "teamId") String teamId) {
        log.info(PROJECT_ADD_TEAM);
        
        projectService.addTeam(projectKey, teamId);

        return new ResponseEntity<>(OK);
    }

    @GetMapping(path = "/{projectKey}/issues")
    public ResponseEntity<List<IssueFullResponse>> getAllIssuesOnProject(@PathVariable(name = "projectKey") String projectKey) {
        log.info("Get issues on project with id: {}", projectKey);

        List<IssueEntity> entities = projectService.getAllIssuesOnProjectById(projectKey);

        List<IssueFullResponse> responses = entities.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());

        return new ResponseEntity<>(
            responses,
            OK
        );
    }
    
    @GetMapping(path = "/{projectKey}/teams")
    public ResponseEntity<List<TeamFullResponse>> getAllTeamsOnProject(@PathVariable(name = "projectKey") String projectKey) {
        log.info("Get teams on project with id: {}", projectKey);

        List<TeamEntity> entities = projectService.getAllTeamsOnProjectById(projectKey);

        List<TeamFullResponse> responses = entities.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());

        return new ResponseEntity<>(
            responses,
            OK
        );
    }
}
