package ro.alexportfolio.backend.service;

import org.springframework.stereotype.Service;
import ro.alexportfolio.backend.dao.ProjectRepository;
import ro.alexportfolio.backend.dao.UserProjectRoleRepository;
import ro.alexportfolio.backend.dao.UserRepository;
import ro.alexportfolio.backend.dto.request.UserProjectAssignmentRequest;
import ro.alexportfolio.backend.exception.ProjectNotFoundException;
import ro.alexportfolio.backend.model.Project;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.model.UserProjectRole;
import ro.alexportfolio.backend.util.Patcher;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserProjectRoleRepository userProjectRoleRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    public ProjectService(ProjectRepository projectRepository,
                          UserProjectRoleRepository userProjectRoleRepository,
                          UserRepository userRepository,
                          Clock clock) {
        this.projectRepository = projectRepository;
        this.userProjectRoleRepository = userProjectRoleRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    public void createProject(Project project) {
        project.setCreatedAt(LocalDateTime.now(clock));

        projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectByProjectKey(String projectKey) {
        return projectRepository.findByProjectKey(projectKey)
                .orElseThrow(ProjectNotFoundException::new);
    }

    public void updateProject(String projectKey, Project project) {
        Project existingProject = projectRepository.findByProjectKey(projectKey)
                .orElseThrow(ProjectNotFoundException::new);

        existingProject.setName(project.getName());
        existingProject.setDescription(project.getDescription());

        projectRepository.save(existingProject);
    }

    public void deleteProject(String projectKey) {
        projectRepository.deleteByProjectKey(projectKey);
    }

    public void partialUpdateProject(String projectKey, Map<String, Object> updates) {
        Project existingProject = projectRepository.findByProjectKey(projectKey)
                .orElseThrow(ProjectNotFoundException::new);
        
        Patcher.patch(existingProject, updates);

        // existingProject.setUpdatedAt(LocalDateTime.now(clock));
        projectRepository.save(existingProject);
    }

    public List<User> getUsersAssignedToProject(String projectKey) {
        List<UserProjectRole> userProjectRoles = userProjectRoleRepository.findByProjectKey(projectKey);

        return userProjectRoles.stream()
                .map(role -> userRepository.findByUserId(role.getUserId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public void assignUsersToProject(List<UserProjectAssignmentRequest> assignments, String projectKey) {
        if (!projectRepository.existsByProjectKey(projectKey)) {
            throw new IllegalArgumentException("Project not found");
        }

        for (UserProjectAssignmentRequest assignment : assignments) {
            String userId = assignment.userId();
            String role = assignment.roleName();

            if (userProjectRoleRepository.existsByUserIdAndProjectKey(userId, projectKey)) {
                throw new IllegalArgumentException("User already assigned to this project");
            }

            if (!userRepository.existsByUserId(userId)) {
                throw new IllegalArgumentException("User not found");
            }

            UserProjectRole userProjectRole = new UserProjectRole();
            userProjectRole.setUserId(userId);
            userProjectRole.setProjectKey(projectKey);
            userProjectRole.setRoleName(role);
            userProjectRole.setCreatedAt(LocalDateTime.now(clock));

            userProjectRoleRepository.save(userProjectRole);
        }
    }

    public List<User> getUnassignedUsers(String projectKey) {
        List<UserProjectRole> userProjectRoles = userProjectRoleRepository.findByProjectKey(projectKey);

        return userRepository.findAll().stream()
            .filter(user -> userProjectRoles.stream()
                .noneMatch(role -> role.getUserId().equals(user.getUserId())))
            .toList();
    }

    public List<String> getProjectUserRoles(String projectKey) {
        return userProjectRoleRepository.findByProjectKey(projectKey).stream()
                .map(UserProjectRole::getRoleName)
                .distinct()
                .toList();
    }
}
