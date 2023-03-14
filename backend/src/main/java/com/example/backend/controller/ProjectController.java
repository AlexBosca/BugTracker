package com.example.backend.controller;

import com.example.backend.dto.request.ProjectRequest;
import com.example.backend.dto.response.ProjectFullResponse;
import com.example.backend.entity.ProjectEntity;
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

    @GetMapping(path = "/{projectId}")
    public ResponseEntity<ProjectFullResponse> getProject(@PathVariable(name = "projectId") String projectId) {
        log.info(PROJECT_GET_BY_ID, projectId);
        
        return new ResponseEntity<>(
                mapper.toResponse(projectService.getProjectByProjectId(projectId)),
                OK
        );
    }

    @PostMapping
    public ResponseEntity<Void> createProject(@RequestBody ProjectRequest request) {
        log.info(PROJECT_CREATE_NEW);
        
        projectService.saveProject(mapper.toEntity(request));

        return new ResponseEntity<>(CREATED);
    }

    @PutMapping(path = "/{projectId}/addTeam/{teamId}")
    public ResponseEntity<Void> addTeamToProject(@PathVariable(name = "projectId") String projectId, @PathVariable(name = "teamId") String teamId) {
        log.info(PROJECT_ADD_TEAM);
        
        projectService.addTeam(projectId, teamId);

        return new ResponseEntity<>(OK);
    }
}
