package com.example.backend.controller;

import com.example.backend.dto.request.ProjectRequest;
import com.example.backend.dto.response.ProjectFullResponse;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping(path = "project")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final MapStructMapper mapper;

    @GetMapping(path = "/{projectId}")
    public ResponseEntity<ProjectFullResponse> getProject(@PathVariable(name = "projectId") String projectId) {
        return new ResponseEntity<>(
                mapper.toResponse(projectService.getProjectByProjectId(projectId)),
                OK
        );
    }

    @PostMapping
    public ResponseEntity<Void> createProject(@RequestBody ProjectRequest request) {
        projectService.saveProject(mapper.toEntity(request));

        return new ResponseEntity<>(CREATED);
    }

    @PutMapping(path = "/{projectId}/addTeam/{teamId}")
    public ResponseEntity<Void> addTeamToProject(@PathVariable(name = "projectId") String projectId, @PathVariable(name = "teamId") String teamId) {
        projectService.addTeam(projectId, teamId);

        return new ResponseEntity<>(OK);
    }
}
