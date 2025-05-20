package ro.alexportfolio.backend.unittests.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ro.alexportfolio.backend.controller.ProjectController;
import ro.alexportfolio.backend.dto.request.ProjectRequestDTO;
import ro.alexportfolio.backend.dto.response.ProjectResponseDTO;
import ro.alexportfolio.backend.mapper.ProjectMapper;
import ro.alexportfolio.backend.model.Project;
import ro.alexportfolio.backend.service.ProjectService;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {
    
    @Mock
    private ProjectService projectService;

    @Mock
    private ProjectMapper mapper;

    @InjectMocks
    private ProjectController projectController;

    @Test
    void testCreateProject() {
        ProjectRequestDTO request = new ProjectRequestDTO("TEST", "Test project", "Test description");
        Project project = new Project();
        project.setProjectKey("TEST");
        project.setName("Test project");
        project.setDescription("Test description");

        when(mapper.toEntity(request)).thenReturn(project);
        doNothing().when(projectService).createProject(project);

        ResponseEntity<Void> responseEntity = projectController.createProject(request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(projectService).createProject(project);
    }

    @Test
    void testGetAllProjects() {
        Project firstProject = new Project("TEST", "Test project", "Test description");
        Project secondProject = new Project("TEST2", "Test project 2", "Test description 2");

        List<Project> projects = List.of(firstProject, secondProject);
        List<ProjectResponseDTO> responseList = List.of(new ProjectResponseDTO("TEST", "Test project", "Test description"),
                new ProjectResponseDTO("TEST2", "Test project 2", "Test description 2"));

        when(projectService.getAllProjects()).thenReturn(projects);
        when(mapper.toResponseList(projects)).thenReturn(responseList);

        ResponseEntity<List<ProjectResponseDTO>> responseEntity = projectController.getAllProjects();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(responseList);
    }

    @Test
    void testGetProject() {
        Project project = new Project("TEST", "Test project", "Test description");
        ProjectResponseDTO responseDTO = new ProjectResponseDTO("TEST", "Test project", "Test description");

        when(projectService.getProjectByProjectKey("TEST")).thenReturn(project);
        when(mapper.toResponse(project)).thenReturn(responseDTO);

        ResponseEntity<ProjectResponseDTO> responseEntity = projectController.getProject("TEST");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(responseDTO);
    }

    @Test
    void testUpdateProject() {
        ProjectRequestDTO request = new ProjectRequestDTO("TEST", "Updated project", "Updated description");
        Project project = new Project();
        project.setProjectKey("TEST");
        project.setName("Updated project");
        project.setDescription("Updated description");

        when(mapper.toEntity(request)).thenReturn(project);
        doNothing().when(projectService).updateProject("TEST", project);

        ResponseEntity<Void> responseEntity = projectController.updateProject("TEST", request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(projectService).updateProject("TEST", project);
    }

    @Test
    void testDeleteProject() {
        doNothing().when(projectService).deleteProject("TEST");

        ResponseEntity<Void> responseEntity = projectController.deleteProject("TEST");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(projectService).deleteProject("TEST");
    }
}
