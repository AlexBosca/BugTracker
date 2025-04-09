package ro.alexportfolio.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.alexportfolio.backend.dto.request.ProjectRequestDTO;
import ro.alexportfolio.backend.dto.response.ProjectResponseDTO;
import ro.alexportfolio.backend.mapper.ProjectMapper;
import ro.alexportfolio.backend.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    private final ProjectMapper mapper;

    public ProjectController(ProjectService projectService, ProjectMapper mapper) {
        this.projectService = projectService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<Void> createProject(@RequestBody ProjectRequestDTO request) {
        projectService.createProject(mapper.toEntity(request));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return new ResponseEntity<>(
                mapper.toResponseList(projectService.getAllProjects()),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{projectKey}")
    public ResponseEntity<ProjectResponseDTO> getProject(@PathVariable(name = "projectKey") String projectKey) {
        return new ResponseEntity<>(
                mapper.toResponse(projectService.getProjectByProjectKey(projectKey)),
                HttpStatus.OK
        );
    }

    @PutMapping(path = "{projectKey}")
    public ResponseEntity<Void> updateProject(@PathVariable(name = "projectKey") String projectKey,
                                              @RequestBody ProjectRequestDTO request) {
        projectService.updateProject(projectKey, mapper.toEntity(request));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "{projectKey}")
    public ResponseEntity<Void> deleteProject(@PathVariable(name = "projectKey") String projectKey) {
        projectService.deleteProject(projectKey);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
