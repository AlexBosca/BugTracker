package ro.alexportfolio.backend.unittests.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.alexportfolio.backend.dao.ProjectRepository;
import ro.alexportfolio.backend.exception.ExceptionMessages;
import ro.alexportfolio.backend.exception.ProjectNotFoundException;
import ro.alexportfolio.backend.model.Project;
import ro.alexportfolio.backend.service.ProjectService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private Clock clock;

    @Captor
    private ArgumentCaptor<Project> projectCaptor;

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
        this.projectService = new ProjectService(projectRepository, clock);
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

        when(projectRepository.findByProjectKey(projectKey)).thenReturn(java.util.Optional.empty());

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

        when(projectRepository.findByProjectKey(projectKey)).thenReturn(java.util.Optional.of(existingProject));

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

        when(projectRepository.findByProjectKey(projectKey)).thenReturn(java.util.Optional.empty());

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
}
