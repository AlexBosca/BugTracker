package ro.alexportfolio.backend.unittests.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.alexportfolio.backend.dao.ProjectRepository;
import ro.alexportfolio.backend.dao.UserProjectRoleRepository;
import ro.alexportfolio.backend.dao.UserRepository;
import ro.alexportfolio.backend.dto.request.UserProjectAssignmentRequest;
import ro.alexportfolio.backend.exception.ExceptionMessages;
import ro.alexportfolio.backend.exception.ProjectNotFoundException;
import ro.alexportfolio.backend.model.Project;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.model.UserProjectRole;
import ro.alexportfolio.backend.service.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserProjectRoleRepository userProjectRoleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Clock clock;

    @Captor
    private ArgumentCaptor<Project> projectCaptor;

    @Captor
    private ArgumentCaptor<UserProjectRole> userProjectRoleCaptor;

    private ProjectService projectService;
    
    private static final ZonedDateTime NOW = ZonedDateTime.of(2022,
                                                       12,
                                                       26,
                                                       11,
                                                       30,
                                                       0,
                                                       0,
                                                       ZoneId.of("GMT"));

    @BeforeEach
    void setUp() {
        this.projectService = new ProjectService(projectRepository, userProjectRoleRepository, userRepository, clock);
    }

    @Test
    void createProject_NonExistingProject() {
        // Given
        Project project = new Project("projectKey", "Project Name", "Project Description");

        // When
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(clock.getZone()).thenReturn(NOW.getZone());

        projectService.createProject(project);

        // Then
        verify(projectRepository, times(1)).save(projectCaptor.capture());

        Project capturedProject = projectCaptor.getValue();

        assertThat(capturedProject.getProjectKey()).isEqualTo("projectKey");
        assertThat(capturedProject.getName()).isEqualTo("Project Name");
        assertThat(capturedProject.getDescription()).isEqualTo("Project Description");
    }

    @Test
    void getAllProjects() {
        // Given
        Project project1 = new Project("projectKey1", "Project Name 1", "Project Description 1");
        Project project2 = new Project("projectKey2", "Project Name 2", "Project Description 2");

        when(projectRepository.findAll()).thenReturn(List.of(project1, project2));

        // When
        List<Project> projects = projectService.getAllProjects();

        // Then
        assertThat(projects).hasSize(2).containsExactlyInAnyOrder(project1, project2);
    }

    @Test
    void getProjectByProjectKey_ExistingProject() {
        // Given
        String projectKey = "projectKey";
        Project project = new Project(projectKey, "Project Name", "Project Description");

        when(projectRepository.findByProjectKey(projectKey)).thenReturn(java.util.Optional.of(project));

        // When
        Project foundProject = projectService.getProjectByProjectKey(projectKey);

        // Then
        assertThat(foundProject).isEqualTo(project);
    }

    @Test
    void getProjectByProjectKey_NonExistingProject() {
        // Given
        String projectKey = "nonExistingProjectKey";

        when(projectRepository.findByProjectKey(projectKey)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> projectService.getProjectByProjectKey(projectKey))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage(ExceptionMessages.PROJECT_NOT_FOUND.getMessage());
    }

    @Test
    void updateProject_ExistingProject() {
        // Given
        String projectKey = "projectKey";
        Project existingProject = new Project(projectKey, "Old Name", "Old Description");
        Project updatedProject = new Project(projectKey, "New Name", "New Description");

        when(projectRepository.findByProjectKey(projectKey)).thenReturn(Optional.of(existingProject));

        // When
        projectService.updateProject(projectKey, updatedProject);

        // Then
        verify(projectRepository, times(1)).save(projectCaptor.capture());

        Project capturedProject = projectCaptor.getValue();

        assertThat(capturedProject.getName()).isEqualTo("New Name");
        assertThat(capturedProject.getDescription()).isEqualTo("New Description");
    }

    @Test
    void updateProject_NonExistingProject() {
        // Given
        String projectKey = "nonExistingProjectKey";
        Project updatedProject = new Project(projectKey, "New Name", "New Description");

        when(projectRepository.findByProjectKey(projectKey)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> projectService.updateProject(projectKey, updatedProject))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage(ExceptionMessages.PROJECT_NOT_FOUND.getMessage());
    }

    @Test
    void deleteProject_ExistingProject() {
        // Given
        String projectKey = "projectKey";

        // When

        projectService.deleteProject(projectKey);

        // Then
        verify(projectRepository, times(1)).deleteByProjectKey(projectKey);
    }

    @Test
    void partialUpdateProject_ExistingProject() {
        // Given
        String projectKey = "projectKey";
        Project existingProject = new Project(projectKey, "Old Name", "Old Description");

        when(projectRepository.findByProjectKey(projectKey)).thenReturn(Optional.of(existingProject));

        // When
        projectService.partialUpdateProject(projectKey, Map.of("name", "New Name"));

        // Then
        verify(projectRepository, times(1)).save(projectCaptor.capture());

        Project capturedProject = projectCaptor.getValue();

        assertThat(capturedProject.getName()).isEqualTo("New Name");
    }

    @Test
    void partialUpdateProject_NonExistingProject() {
        // Given
        String projectKey = "nonExistingProjectKey";

        when(projectRepository.findByProjectKey(projectKey)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> projectService.partialUpdateProject(projectKey, Map.of("name", "New Name")))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage(ExceptionMessages.PROJECT_NOT_FOUND.getMessage());
    }

    @Test
    void getUsersAssignedToProject_ExistingProject() {
        // Given
        String projectKey = "projectKey";
        User user1 = new User("userId1", "User Name 1", "User Last Name 1", "", projectKey, null);
        User user2 = new User("userId2", "User Name 2", "User Last Name 2", "", projectKey, null);
        UserProjectRole role1 = new UserProjectRole("userId1", projectKey, "roleName1");
        UserProjectRole role2 = new UserProjectRole("userId2", projectKey, "roleName2");

        when(userProjectRoleRepository.findByProjectKey(projectKey)).thenReturn(List.of(role1, role2));
        when(userRepository.findByUserId("userId1")).thenReturn(Optional.of(user1));
        when(userRepository.findByUserId("userId2")).thenReturn(Optional.of(user2));

        // When
        List<User> assignedUsers = projectService.getUsersAssignedToProject(projectKey);

        // Then
        assertThat(assignedUsers).hasSize(2).containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    void assignUsersToProject_ValidAssignments() {
        // Given
        String projectKey = "projectKey";
        UserProjectAssignmentRequest assignment1 = new UserProjectAssignmentRequest("userId1", "roleName1");
        UserProjectAssignmentRequest assignment2 = new UserProjectAssignmentRequest("userId2", "roleName2");

        when(projectRepository.existsByProjectKey(projectKey)).thenReturn(true);
        when(userProjectRoleRepository.existsByUserIdAndProjectKey("userId1", projectKey)).thenReturn(false);
        when(userProjectRoleRepository.existsByUserIdAndProjectKey("userId2", projectKey)).thenReturn(false);
        when(userRepository.existsByUserId("userId1")).thenReturn(true);
        when(userRepository.existsByUserId("userId2")).thenReturn(true);

        // When
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(clock.getZone()).thenReturn(NOW.getZone());
        projectService.assignUsersToProject(List.of(assignment1, assignment2), projectKey);

        // Then
        verify(userProjectRoleRepository, times(2)).save(userProjectRoleCaptor.capture());

        List<UserProjectRole> capturedRoles = userProjectRoleCaptor.getAllValues();

        assertThat(capturedRoles).hasSize(2);

        UserProjectRole role1 = capturedRoles.get(0);
        UserProjectRole role2 = capturedRoles.get(1);

        assertThat(role1.getUserId()).isEqualTo("userId1");
        assertThat(role1.getProjectKey()).isEqualTo(projectKey);
        assertThat(role1.getRoleName()).isEqualTo("roleName1");

        assertThat(role2.getUserId()).isEqualTo("userId2");
        assertThat(role2.getProjectKey()).isEqualTo(projectKey);
        assertThat(role2.getRoleName()).isEqualTo("roleName2");
    }

    @Test
    void assignUsersToProject_ProjectNotFound() {
        // Given
        String projectKey = "nonExistingProjectKey";
        UserProjectAssignmentRequest assignment = new UserProjectAssignmentRequest("userId", "roleName");

        // When & Then
        when(projectRepository.existsByProjectKey(projectKey)).thenReturn(false);
        
        assertThatThrownBy(() -> projectService.assignUsersToProject(List.of(assignment), projectKey))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Project not found");
    }

    @Test
    void assignUsersToProject_UserAlreadyAssigned() {
        // Given
        String projectKey = "projectKey";
        UserProjectAssignmentRequest assignment = new UserProjectAssignmentRequest("userId", "roleName");

        when(projectRepository.existsByProjectKey(projectKey)).thenReturn(true);
        when(userProjectRoleRepository.existsByUserIdAndProjectKey("userId", projectKey)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> projectService.assignUsersToProject(List.of(assignment), projectKey))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User already assigned to this project");
    }

    @Test
    void assignUsersToProject_UserNotFound() {
        // Given
        String projectKey = "projectKey";
        UserProjectAssignmentRequest assignment = new UserProjectAssignmentRequest("nonExistingUserId", "roleName");

        when(projectRepository.existsByProjectKey(projectKey)).thenReturn(true);
        when(userProjectRoleRepository.existsByUserIdAndProjectKey("nonExistingUserId", projectKey)).thenReturn(false);
        when(userRepository.existsByUserId("nonExistingUserId")).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> projectService.assignUsersToProject(List.of(assignment), projectKey))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");
    }

    @Test
    void getUnassignedUsers_ExistingProject() {
        // Given
        String projectKey = "projectKey";
        UserProjectRole userProjectRole = new UserProjectRole("userId1", projectKey, "roleName");
        User user1 = new User("userId1", "User Name 1", "User Last Name 1", "", projectKey, null);
        User user2 = new User("userId2", "User Name 2", "User Last Name 2", "", projectKey, null);

        when(userProjectRoleRepository.findByProjectKey(projectKey)).thenReturn(List.of(userProjectRole));
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // When
        List<User> unassignedUsers = projectService.getUnassignedUsers(projectKey);

        // Then
        assertThat(unassignedUsers).hasSize(1).containsExactly(user2);
    }

    @Test
    void getProjectUserRoles_ExistingProject() {
        // Given
        String projectKey = "projectKey";
        UserProjectRole role1 = new UserProjectRole("userId1", projectKey, "roleName1");
        UserProjectRole role2 = new UserProjectRole("userId2", projectKey, "roleName2");
        UserProjectRole role3 = new UserProjectRole("userId3", projectKey, "roleName1"); // Duplicate role name

        when(userProjectRoleRepository.findByProjectKey(projectKey)).thenReturn(List.of(role1, role2, role3));

        // When
        List<String> roles = projectService.getProjectUserRoles(projectKey);

        // Then
        assertThat(roles).hasSize(2).containsExactlyInAnyOrder("roleName1", "roleName2");
    }
}
