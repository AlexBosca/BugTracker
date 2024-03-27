package com.example.backend.controller;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.ProjectRequest;
import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.dto.response.ProjectFullResponse;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.backend.util.project.ProjectLoggingMessages.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final MapStructMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProjectFullResponse>> getAllProjects() {
        List<ProjectEntity> projects = projectService.getAllProjects();
        logInfo(PROJECT_ALL_RETRIEVED, projects);

        List<ProjectFullResponse> projectsResponses = projects.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());

        return new ResponseEntity<>(projectsResponses, OK);
    }

    @PostMapping(path = "/filter")
    public ResponseEntity<List<ProjectFullResponse>> getFilteredProjects(@RequestBody FilterCriteria filterCriteria) {
        List<ProjectEntity> projects = projectService.filterProjects(filterCriteria);
        logInfo(PROJECT_FILTERED_RETRIEVED, projects);

        List<ProjectFullResponse> projectsResponses = projects.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());

        return new ResponseEntity<>(projectsResponses, OK);
    }

    @GetMapping(path = "/{projectKey}")
    public ResponseEntity<ProjectFullResponse> getProject(@PathVariable(name = "projectKey") String projectKey) {
        ProjectEntity project = projectService.getProjectByProjectKey(projectKey);
        logInfo(PROJECT_RETRIEVED, project);

        ProjectFullResponse projectResponse = mapper.toResponse(project);
        
        return new ResponseEntity<>(projectResponse, OK);
    }

    @PostMapping
    public ResponseEntity<Void> createProject(@Valid @RequestBody ProjectRequest request) {
        ProjectEntity project = mapper.toEntity(request);
        
        projectService.saveProject(project);
        logInfo(PROJECT_CREATED, project);

        return new ResponseEntity<>(CREATED);
    }

    @GetMapping(path = "/{projectKey}/issues")
    public ResponseEntity<List<IssueFullResponse>> getAllIssuesOnProject(@PathVariable(name = "projectKey") String projectKey) {
        List<IssueEntity> issues = projectService.getAllIssuesOnProjectById(projectKey);
        logInfo(PROJECT_ISSUES_RETRIEVED, issues);

        List<IssueFullResponse> issuesResponses = issues.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());

        return new ResponseEntity<>(issuesResponses, OK);
    }

    private void logInfo(String var1, Object var2) {
        log.info(var1, "Controller", var2);
    }
}
