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

    public ProjectController(final ProjectService projectServiceParam,
                             final ProjectMapper projectMapperParam,
                             final UserMapper userMapperParam) {
        this.projectService = projectServiceParam;
        this.projectMapper = projectMapperParam;
        this.userMapper = userMapperParam;
    }

    @PostMapping
    public ResponseEntity<Void> createProject(final @RequestBody ProjectRequestDTO request) {
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
    public ResponseEntity<ProjectResponseDTO> getProject(final @PathVariable(name = "projectKey") String projectKey) {
        return new ResponseEntity<>(
                projectMapper.toResponse(projectService.getProjectByProjectKey(projectKey)),
                HttpStatus.OK
        );
    }

    @PutMapping(path = "/{projectKey}")
    public ResponseEntity<Void> updateProject(final @PathVariable(name = "projectKey") String projectKey,
                                              final @RequestBody ProjectRequestDTO request) {
        projectService.updateProject(projectKey, projectMapper.toEntity(request));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/{projectKey}")
    public ResponseEntity<Void> patchProject(final @PathVariable(name = "projectKey") String projectKey,
                                             final @RequestBody ProjectRequestDTO request) {
        projectService.partialUpdateProject(projectKey, RecordMapper.toMap(request));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{projectKey}")
    public ResponseEntity<Void> deleteProject(final @PathVariable(name = "projectKey") String projectKey) {
        projectService.deleteProject(projectKey);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/{projectKey}/users")
    public ResponseEntity<List<UserResponseDTO>> getProjectUsers(final @PathVariable(name = "projectKey") String projectKey) {
        return new ResponseEntity<>(
                userMapper.toResponseList(projectService.getUsersAssignedToProject(projectKey)),
                HttpStatus.OK
        );
    }

    @PostMapping(path = "/{projectKey}/users")
    public ResponseEntity<Void> assignUserToProject(final @PathVariable(name = "projectKey") String projectKey,
                                                    final @RequestBody UserProjectBatchAssignmentRequest request) {
        this.projectService.assignUsersToProject(request.userProjectAssignments(), projectKey);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/{projectKey}/users/unassigned")
    public ResponseEntity<List<UserResponseDTO>> getUnassignedUsers(final @PathVariable(name = "projectKey") String projectKey) {
        return new ResponseEntity<>(
                userMapper.toResponseList(projectService.getUnassignedUsers(projectKey)),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{projectKey}/roles")
    public ResponseEntity<List<String>> getProjectUserRoles(final @PathVariable(name = "projectKey") String projectKey) {
        return new ResponseEntity<>(
                projectService.getProjectUserRoles(projectKey),
                HttpStatus.OK
        );
    }
}
