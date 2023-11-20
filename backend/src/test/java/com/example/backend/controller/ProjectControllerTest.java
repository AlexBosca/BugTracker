package com.example.backend.controller;

import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.dto.response.ProjectFullResponse;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static com.example.backend.util.ExceptionUtilities.PROJECT_WITH_ID_NOT_FOUND;

// @Disabled
@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ProjectController.class, useDefaultFilters = false, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(ProjectController.class)
@ComponentScan({"com.example.backend.mapper"})
class ProjectControllerTest {

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
    void getAllProjects_ExistingIssues_OkResponse() throws Exception {
        ProjectEntity firstExpectedProject = ProjectEntity.builder()
            .projectKey("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .build();

        ProjectEntity secondExpectedProject = ProjectEntity.builder()
            .projectKey("PROJECT2")
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
    void getAllProjects_NoIssues_OkResponse() throws Exception {
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
    void getProject_NoExceptionThrown_OkResponse() throws Exception {
        String givenProjectKey = "PROJECT1";

        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .build();

        when(projectService.getProjectByProjectKey(givenProjectKey)).thenReturn(expectedProject);

        ProjectFullResponse expectedProjectResponse = mapStructMapper.toResponse(expectedProject);

        mockMvc.perform(get("/projects/{projectKey}", givenProjectKey))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedProjectResponse)));
    }

    @Test
    @DisplayName("Should return OK status and a not empty list when there are issues on project by given projectKey")
    void getIssuesOnProject_ExistingIssuesOnProject_OkResponse() throws Exception {
        String givenProjectKey = "PROJECT1";

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
            .projectKey("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .issues(expectedIssues)
            .build();

        when(projectService.getProjectByProjectKey(givenProjectKey)).thenReturn(expectedProject);
        when(projectService.getAllIssuesOnProjectById(givenProjectKey)).thenReturn(expectedIssues);

        List<IssueFullResponse> expectesIssuesResponses = List.of(
            firstExpectedIssueResponse,
            secondExpectedIssueResponse
        );

        mockMvc.perform(get("/projects/{projectKey}/issues", givenProjectKey))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectesIssuesResponses)));
    }

    @Test
    @DisplayName("Should return OK status and an empty list when there are no issues on project by given projectKey")
    void getIssuesOnProject_NoIssuesOnProject_OkResponse() throws Exception {
        String givenProjectKey = "PROJECT1";

        List<IssueEntity> expectedIssues = List.of();
        
        when(projectService.getAllIssuesOnProjectById(givenProjectKey)).thenReturn(expectedIssues);

        List<IssueFullResponse> expectesIssuesResponses = List.of();

        mockMvc.perform(get("/projects/{projectKey}/issues", givenProjectKey))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectesIssuesResponses)));
    }

    @Test
    @DisplayName("Should return NOT_FOUND status and throw ProjectNotFoundException when the project to return the issues created on does not exist")
    void getIssuesOnProject_ProjectNotFoundExceptionThrown_OkResponse() throws Exception {
        String givenProjectKey = "PROJECT1";

        when(projectService.getAllIssuesOnProjectById(givenProjectKey)).thenThrow(new ProjectNotFoundException(givenProjectKey));

        mockMvc.perform(get("/projects/{projectKey}/issues", givenProjectKey))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ProjectNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(PROJECT_WITH_ID_NOT_FOUND, "PROJECT1")));
    }
}
