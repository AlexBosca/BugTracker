package com.example.backend.controller;

import com.example.backend.dto.request.IssueRequest;
import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.dto.response.ProjectFullResponse;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.project.ProjectIdNotFoundException;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.IssueService;
import com.example.backend.service.ProjectService;
import com.example.backend.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.example.backend.enums.IssuePriority.LOW;
import static com.example.backend.enums.IssueStatus.NEW;

import static com.example.backend.util.ExceptionUtilities.PROJECT_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.PROJECT_ALREADY_CREATED;
import static com.example.backend.util.ExceptionUtilities.TEAM_WITH_ID_NOT_FOUND;

// @Disabled
@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ProjectController.class, useDefaultFilters = false, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(ProjectController.class)
@ComponentScan({"com.example.backend.mapper"})
public class ProjectControllerTest {

    @Autowired
    private MapStructMapper mapStructMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private Clock clock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return OK status and a not empty list when there are projects")
    public void shouldGetAllProjectsAndOkStatusWhenThereAreProjects() throws Exception {
        ProjectEntity firstExpectedProject = ProjectEntity.builder()
            .projectId("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .build();

        ProjectEntity secondExpectedProject = ProjectEntity.builder()
            .projectId("PROJECT2")
            .name("Second Project")
            .description("Second Project Description")
            .build();

        ProjectFullResponse firstExpectedProjectResponse = mapStructMapper.toResponse(firstExpectedProject);
        ProjectFullResponse secondExpectedProjectResponse = mapStructMapper.toResponse(secondExpectedProject);

        List<ProjectEntity> expectedProjects = List.of(
            firstExpectedProject,
            secondExpectedProject
        );

        when(projectService.getAllProjects()).thenReturn(expectedProjects);

        List<ProjectFullResponse> expectedProjectsResponses = List.of(
            firstExpectedProjectResponse,
            secondExpectedProjectResponse
        );

        mockMvc.perform(get("/projects"))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedProjectsResponses)));
    }

    @Test
    @DisplayName("Should return OK status and an empty list when there are no projects")
    public void shouldGetAllProjectsAndOkStatusWhenThereAreNoProjects() throws Exception {
        List<ProjectEntity> expectedProjects = List.of();

        when(projectService.getAllProjects()).thenReturn(expectedProjects);

        List<ProjectFullResponse> expectedProjectsResponses = List.of();

        mockMvc.perform(get("/projects"))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedProjectsResponses)));
    }

    @Test
    @DisplayName("Should return OK status and an empty list when there are no projects")
    public void shouldGetProjectByProjectIdAndOkStatusWhenProjectExists() throws Exception {
        String givenProjectId = "PROJECT1";

        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectId("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .build();

        when(projectService.getProjectByProjectId(givenProjectId)).thenReturn(expectedProject);

        ProjectFullResponse expectedProjectResponse = mapStructMapper.toResponse(expectedProject);

        mockMvc.perform(get("/projects/{projectId}", givenProjectId))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedProjectResponse)));
    }

    @Test
    @DisplayName("Should return OK status and a not empty list when there are issues on project by given projectId")
    public void shouldGetAllIssuesOnProjectByProjectIdAndOkStatusWhenProjectExistsAndThereAreIssues() throws Exception {
        String givenProjectId = "PROJECT1";

        IssueEntity firstExpectedIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        IssueEntity secondExpectedIssue = IssueEntity.builder()
            .issueId("00002")
            .title("Second Issue Title")
            .description("Second Issue Description")
            .build();

        IssueFullResponse firstExpectedIssueResponse = mapStructMapper.toResponse(firstExpectedIssue);
        IssueFullResponse secondExpectedIssueResponse = mapStructMapper.toResponse(secondExpectedIssue);

        List<IssueEntity> expectedIssues = List.of(
            firstExpectedIssue,
            secondExpectedIssue
        );

        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectId("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .issues(expectedIssues)
            .build();

        when(projectService.getProjectByProjectId(givenProjectId)).thenReturn(expectedProject);
        when(projectService.getAllIssuesOnProjectById(givenProjectId)).thenReturn(expectedIssues);

        List<IssueFullResponse> expectesIssuesResponses = List.of(
            firstExpectedIssueResponse,
            secondExpectedIssueResponse
        );

        mockMvc.perform(get("/projects/{projectId}/issues", givenProjectId))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectesIssuesResponses)));
    }

    @Test
    @DisplayName("Should return OK status and an empty list when there are no issues on project by given projectId")
    public void shouldGetAllIssuesOnProjectByProjectIdAndOkStatusWhenProjectExistsAndThereAreNoIssues() throws Exception {
        String givenProjectId = "PROJECT1";

        List<IssueEntity> expectedIssues = List.of();
        
        when(projectService.getAllIssuesOnProjectById(givenProjectId)).thenReturn(expectedIssues);

        List<IssueFullResponse> expectesIssuesResponses = List.of();

        mockMvc.perform(get("/projects/{projectId}/issues", givenProjectId))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectesIssuesResponses)));
    }

    @Test
    @DisplayName("Should return NOT_FOUND status and throw ProjectIdNotFoundException when the project to return the issues created on does not exist")
    public void shouldThrowProjectIdNotFoundExceptionndNotFoundStatusWhenProjectDoesNotExist() throws Exception {
        String givenProjectId = "PROJECT1";

        when(projectService.getAllIssuesOnProjectById(givenProjectId)).thenThrow(new ProjectIdNotFoundException(givenProjectId));

        mockMvc.perform(get("/projects/{projectId}/issues", givenProjectId))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ProjectIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(PROJECT_WITH_ID_NOT_FOUND, "PROJECT1")));
    }
}
