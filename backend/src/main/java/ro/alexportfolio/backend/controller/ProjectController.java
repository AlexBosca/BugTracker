package ro.alexportfolio.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.alexportfolio.backend.dto.request.ProjectRequestDTO;
import ro.alexportfolio.backend.dto.request.UserProjectBatchAssignmentRequest;
import ro.alexportfolio.backend.dto.response.ProjectResponseDTO;
import ro.alexportfolio.backend.dto.response.UserResponseDTO;
import ro.alexportfolio.backend.mapper.ProjectMapper;
import ro.alexportfolio.backend.mapper.RecordMapper;
import ro.alexportfolio.backend.mapper.UserMapper;
import ro.alexportfolio.backend.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    private final ProjectMapper projectMapper;

    private final UserMapper userMapper;

    public ProjectController(ProjectService projectService, ProjectMapper projectMapper, UserMapper userMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<Void> createProject(@RequestBody ProjectRequestDTO request) {
        projectService.createProject(projectMapper.toEntity(request));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return new ResponseEntity<>(
                projectMapper.toResponseList(projectService.getAllProjects()),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{projectKey}")
    public ResponseEntity<ProjectResponseDTO> getProject(@PathVariable(name = "projectKey") String projectKey) {
        return new ResponseEntity<>(
                projectMapper.toResponse(projectService.getProjectByProjectKey(projectKey)),
                HttpStatus.OK
        );
    }

    @PutMapping(path = "/{projectKey}")
    public ResponseEntity<Void> updateProject(@PathVariable(name = "projectKey") String projectKey,
                                              @RequestBody ProjectRequestDTO request) {
        projectService.updateProject(projectKey, projectMapper.toEntity(request));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/{projectKey}")
    public ResponseEntity<Void> patchProject(@PathVariable(name = "projectKey") String projectKey,
                                             @RequestBody ProjectRequestDTO request) {
        projectService.partialUpdateProject(projectKey, RecordMapper.toMap(request));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{projectKey}")
    public ResponseEntity<Void> deleteProject(@PathVariable(name = "projectKey") String projectKey) {
        projectService.deleteProject(projectKey);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/{projectKey}/users")
    public ResponseEntity<List<UserResponseDTO>> getProjectUsers(@PathVariable(name = "projectKey") String projectKey) {
        return new ResponseEntity<>(
                userMapper.toResponseList(projectService.getUsersAssignedToProject(projectKey)),
                HttpStatus.OK
        );
    }

    @PostMapping(path = "/{projectKey}/users")
    public ResponseEntity<Void> assignUserToProject(@PathVariable(name = "projectKey") String projectKey,
                                                    @RequestBody UserProjectBatchAssignmentRequest request) {
        this.projectService.assignUsersToProject(request.userProjectAssignments(), projectKey);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/{projectKey}/users/unassigned")
    public ResponseEntity<List<UserResponseDTO>> getUnassignedUsers(@PathVariable(name = "projectKey") String projectKey) {
        return new ResponseEntity<>(
                userMapper.toResponseList(projectService.getUnassignedUsers(projectKey)),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{projectKey}/roles")
    public ResponseEntity<List<String>> getProjectUserRoles(@PathVariable(name = "projectKey") String projectKey) {
        return new ResponseEntity<>(
                projectService.getProjectUserRoles(projectKey),
                HttpStatus.OK
        );
    }
}
