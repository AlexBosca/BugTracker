package com.example.backend.controller;

import static com.example.backend.util.ExceptionUtilities.PROJECT_ALREADY_CREATED;
import static com.example.backend.util.ExceptionUtilities.PROJECT_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.USER_ROLE_MISMATCH;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_ID_NOT_FOUND;
import static com.example.backend.util.Utilities.formattedString;
import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_UPDATED;
import static java.time.ZonedDateTime.of;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.ProjectRequest;
import com.example.backend.dto.request.ProjectUpdateRequest;
import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.dto.response.ProjectFullResponse;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.enums.UserRole;
import com.example.backend.exception.project.ProjectAlreadyCreatedException;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;
import com.example.backend.exception.user.UserUnexpectedRoleException;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.ProjectService;
import com.example.backend.util.FilterCriteriaMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Captor
    private ArgumentCaptor<ProjectEntity> projectCaptor;

    @Captor
    private ArgumentCaptor<ProjectUpdateRequest> projectRequestCaptor;

    private static ZonedDateTime NOW = of(2022,
                                            12,
                                            26, 
                                            11, 
                                            30, 
                                            0, 
                                            0, 
                                            ZoneId.of("GMT"));

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
    @DisplayName("Should return OK Response and a not empty list when calling the getFilteredProjects endpoint")
    void getFilteredProjects_ExistingIssues_OkResponse() throws Exception {
        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .build();

        Map<String, Object> filters = new HashMap<>();
        filters.put("projectKey", "PROJECT1");

        Map<String, String> operators = new HashMap<>();
        operators.put("projectKey", "=");

        Map<String, String> dataTypes = new HashMap<>();
        dataTypes.put("projectKey", "string");

        FilterCriteria filterCriteria = new FilterCriteria(
            filters,
            operators,
            dataTypes
        );

        ProjectFullResponse expectedProjectResponse = mapStructMapper.toResponse(expectedProject);

        List<ProjectEntity> expectedProjects = List.of(expectedProject);

        when(projectService.filterProjects(argThat(new FilterCriteriaMatcher(filterCriteria)))).thenReturn(expectedProjects);
        
        List<ProjectFullResponse> expectedProjectResponses = List.of(expectedProjectResponse);

        mockMvc.perform(post("/projects/filter")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterCriteria)))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedProjectResponses)));
    }

    @Test
    @DisplayName("Should return OK Response and an empty list when calling the getFilteredProjects endpoint")
    void getFilteredProjects_NoIssues_OkResponse() throws Exception {
        Map<String, Object> filters = new HashMap<>();
        filters.put("projectKey", "PROJECT1");

        Map<String, String> operators = new HashMap<>();
        operators.put("projectKey", "=");

        Map<String, String> dataTypes = new HashMap<>();
        dataTypes.put("projectKey", "string");

        FilterCriteria filterCriteria = new FilterCriteria(
            filters,
            operators,
            dataTypes
        );

        List<ProjectEntity> expectedProjects = List.of();

        when(projectService.filterProjects(argThat(new FilterCriteriaMatcher(filterCriteria)))).thenReturn(expectedProjects);
        
        List<ProjectFullResponse> expectedProjectResponses = List.of();

        mockMvc.perform(post("/projects/filter")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterCriteria)))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedProjectResponses)));
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the getProject endpoint")
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
    @DisplayName("Should return NOT FOUND Response and resolve ProjectNotFoundException when calling the getProject endpoint for a non existing project")
    void getProject_ProjectNotFoundExceptionThrown_NotFoundResponse() throws Exception {
        String givenProjectKey = "PROJECT1";

        when(projectService.getProjectByProjectKey(givenProjectKey)).thenThrow(new ProjectNotFoundException(givenProjectKey));

        mockMvc.perform(get("/projects/{projectKey}", givenProjectKey))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ProjectNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(PROJECT_WITH_ID_NOT_FOUND, "PROJECT1")));
    }

    @Test
    @DisplayName("Should return CREATED Response when no exception was thrown when calling the createProject endpoint")
    void createProject_NoExceptionThrown_CreatedResponse() throws Exception {
        LocalDateTime expectedStartDate = LocalDateTime.of(2024, 4, 5, 12, 0);
        LocalDateTime expectedTargetEndDate = LocalDateTime.of(2025, 4, 5, 12, 0);

        String exisitngProjectManagerId = "JC_12345";

        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .startDate(expectedStartDate)
            .targetEndDate(expectedTargetEndDate)
            .build();

        ProjectRequest projectRequest = mapStructMapper.toRequest(expectedProject);
        projectRequest.setProjectManagerId(exisitngProjectManagerId);

        mockMvc.perform(post("/projects")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequest)))
            .andExpect(status().isCreated());

        verify(projectService).saveProject(projectCaptor.capture(), eq(exisitngProjectManagerId));

        ProjectEntity actualProject = projectCaptor.getValue();

        assertThat(actualProject.getProjectKey()).isEqualTo(expectedProject.getProjectKey());
        assertThat(actualProject.getName()).isEqualTo(expectedProject.getName());
        assertThat(actualProject.getDescription()).isEqualTo(expectedProject.getDescription());
        assertThat(actualProject.getStartDate()).isEqualTo(expectedProject.getStartDate());
        assertThat(actualProject.getTargetEndDate()).isEqualTo(expectedProject.getTargetEndDate());
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve UserIdNotFoundException when calling the createProject endpoint for a non existing project and a non existing user")
    void createProject_UserIdNotFoundExceptionThrown_NotFoundResponse() throws Exception {
        LocalDateTime expectedStartDate = LocalDateTime.of(2024, 4, 5, 12, 0);
        LocalDateTime expectedTargetEndDate = LocalDateTime.of(2025, 4, 5, 12, 0);

        String exisitngProjectManagerId = "JC_12345";

        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .startDate(expectedStartDate)
            .targetEndDate(expectedTargetEndDate)
            .build();

        ProjectRequest projectRequest = mapStructMapper.toRequest(expectedProject);
        projectRequest.setProjectManagerId(exisitngProjectManagerId);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        doThrow(new UserIdNotFoundException(exisitngProjectManagerId)).when(projectService).saveProject(any(), eq(exisitngProjectManagerId));

        mockMvc.perform(post("/projects")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.status").value(NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value(formattedString(USER_WITH_ID_NOT_FOUND, exisitngProjectManagerId)))
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(USER_WITH_ID_NOT_FOUND, exisitngProjectManagerId)));

        verify(projectService).saveProject(projectCaptor.capture(), eq(exisitngProjectManagerId));

        ProjectEntity actualProject = projectCaptor.getValue();

        assertThat(actualProject.getProjectKey()).isEqualTo(expectedProject.getProjectKey());
        assertThat(actualProject.getName()).isEqualTo(expectedProject.getName());
        assertThat(actualProject.getDescription()).isEqualTo(expectedProject.getDescription());
        assertThat(actualProject.getStartDate()).isEqualTo(expectedProject.getStartDate());
        assertThat(actualProject.getTargetEndDate()).isEqualTo(expectedProject.getTargetEndDate());
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve UserUnexpectedRoleException when calling the createProject endpoint for a non existing project and an existing use without ROLE_PROJECT_MANAGER")
    void createProject_UserUnexpectedRoleExceptionThrown_NotFoundResponse() throws Exception {
        LocalDateTime expectedStartDate = LocalDateTime.of(2024, 4, 5, 12, 0);
        LocalDateTime expectedTargetEndDate = LocalDateTime.of(2025, 4, 5, 12, 0);

        String exisitngProjectManagerId = "JC_12345";

        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .startDate(expectedStartDate)
            .targetEndDate(expectedTargetEndDate)
            .build();

        ProjectRequest projectRequest = mapStructMapper.toRequest(expectedProject);
        projectRequest.setProjectManagerId(exisitngProjectManagerId);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        doThrow(new UserUnexpectedRoleException(exisitngProjectManagerId, UserRole.ROLE_DEVELOPER.getName(), UserRole.ROLE_PROJECT_MANAGER.getName())).when(projectService).saveProject(any(), eq(exisitngProjectManagerId));

        mockMvc.perform(post("/projects")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequest)))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.status").value(FORBIDDEN.name()))
            .andExpect(jsonPath("$.message").value(formattedString(USER_ROLE_MISMATCH, exisitngProjectManagerId, UserRole.ROLE_DEVELOPER.getName(),  UserRole.ROLE_PROJECT_MANAGER.getName())))
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserUnexpectedRoleException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(USER_ROLE_MISMATCH, exisitngProjectManagerId, UserRole.ROLE_DEVELOPER.getName(),  UserRole.ROLE_PROJECT_MANAGER.getName())));

        verify(projectService).saveProject(projectCaptor.capture(), eq(exisitngProjectManagerId));

        ProjectEntity actualProject = projectCaptor.getValue();

        assertThat(actualProject.getProjectKey()).isEqualTo(expectedProject.getProjectKey());
        assertThat(actualProject.getName()).isEqualTo(expectedProject.getName());
        assertThat(actualProject.getDescription()).isEqualTo(expectedProject.getDescription());
        assertThat(actualProject.getStartDate()).isEqualTo(expectedProject.getStartDate());
        assertThat(actualProject.getTargetEndDate()).isEqualTo(expectedProject.getTargetEndDate());
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve ProjectAlreadyCreatedException when calling the createProject endpoint for an already created project")
    void createProject_ProjectAlreadyCreated_ResolveProjectAlreadyCreatedExceptionAndBadRequestResponse() throws Exception {
        String givenProjectKey = "PROJECT1";
        String exisitngProjectManagerId = "JC_12345";

        LocalDateTime expectedStartDate = LocalDateTime.of(2024, 4, 5, 12, 0);
        LocalDateTime expectedTargetEndDate = LocalDateTime.of(2025, 4, 5, 12, 0);

        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .startDate(expectedStartDate)
            .targetEndDate(expectedTargetEndDate)
            .build();

        ProjectRequest projectRequest = mapStructMapper.toRequest(expectedProject);
        projectRequest.setProjectManagerId(exisitngProjectManagerId);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        doThrow(new ProjectAlreadyCreatedException(givenProjectKey)).when(projectService).saveProject(any(), eq(exisitngProjectManagerId));

        mockMvc.perform(post("/projects")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value(formattedString(PROJECT_ALREADY_CREATED, givenProjectKey)))
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ProjectAlreadyCreatedException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(PROJECT_ALREADY_CREATED, givenProjectKey)));

        verify(projectService).saveProject(projectCaptor.capture(), eq(exisitngProjectManagerId));

        ProjectEntity actualProject = projectCaptor.getValue();

        assertThat(actualProject.getProjectKey()).isEqualTo(expectedProject.getProjectKey());
        assertThat(actualProject.getName()).isEqualTo(expectedProject.getName());
        assertThat(actualProject.getDescription()).isEqualTo(expectedProject.getDescription());
        assertThat(actualProject.getStartDate()).isEqualTo(expectedProject.getStartDate());
        assertThat(actualProject.getTargetEndDate()).isEqualTo(expectedProject.getTargetEndDate());
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve MethodArgumentNotValidException when calling the createProject endpoint and project key is missing")
    void createProject_ProjectKeyMissing_ResolveMethodArgumentNotValidExceptionAndBadRequestResponse() throws Exception {
        LocalDateTime expectedStartDate = LocalDateTime.of(2024, 4, 5, 12, 0);
        LocalDateTime expectedTargetEndDate = LocalDateTime.of(2025, 4, 5, 12, 0);

        ProjectEntity expectedProject = ProjectEntity.builder()
            .name("First Project")
            .description("First Project Description")
            .startDate(expectedStartDate)
            .targetEndDate(expectedTargetEndDate)
            .build();

        ProjectRequest projectRequest = mapStructMapper.toRequest(expectedProject);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        mockMvc.perform(post("/projects")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").exists())      // TODO: replace exists method call with value method call with actual message value
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isNotBlank());
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve MethodArgumentNotValidException when calling the createProject endpoint and project name is missing")
    void createProject_ProjectNameMissing_ResolveMethodArgumentNotValidExceptionAndBadRequestResponse() throws Exception {
        LocalDateTime expectedStartDate = LocalDateTime.of(2024, 4, 5, 12, 0);
        LocalDateTime expectedTargetEndDate = LocalDateTime.of(2025, 4, 5, 12, 0);

        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("PROJECT1")
            .description("First Project Description")
            .startDate(expectedStartDate)
            .targetEndDate(expectedTargetEndDate)
            .build();

        ProjectRequest projectRequest = mapStructMapper.toRequest(expectedProject);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        mockMvc.perform(post("/projects")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").exists())      // TODO: replace exists method call with value method call with actual message value
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isNotBlank());
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve MethodArgumentNotValidException when calling the createProject endpoint and project description is missing")
    void createProject_ProjectDescriptionMissing_ResolveMethodArgumentNotValidExceptionAndBadRequestResponse() throws Exception {
        LocalDateTime expectedStartDate = LocalDateTime.of(2024, 4, 5, 12, 0);
        LocalDateTime expectedTargetEndDate = LocalDateTime.of(2025, 4, 5, 12, 0);

        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("PROJECT1")
            .name("First Project")
            .startDate(expectedStartDate)
            .targetEndDate(expectedTargetEndDate)
            .build();

        ProjectRequest projectRequest = mapStructMapper.toRequest(expectedProject);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        mockMvc.perform(post("/projects")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").exists())      // TODO: replace exists method call with value method call with actual message value
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isNotBlank());
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve MethodArgumentNotValidException when calling the createProject endpoint and project start date is missing")
    void createProject_ProjectStartDateMissing_ResolveMethodArgumentNotValidExceptionAndBadRequestResponse() throws Exception {
        LocalDateTime expectedTargetEndDate = LocalDateTime.of(2025, 4, 5, 12, 0);

        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .targetEndDate(expectedTargetEndDate)
            .build();

        ProjectRequest projectRequest = mapStructMapper.toRequest(expectedProject);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        mockMvc.perform(post("/projects")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").exists())      // TODO: replace exists method call with value method call with actual message value
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isNotBlank());
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve MethodArgumentNotValidException when calling the createProject endpoint and project target end date is missing")
    void createProject_ProjectTargetEndDateMissing_ResolveMethodArgumentNotValidExceptionAndBadRequestResponse() throws Exception {
        LocalDateTime expectedStartDate = LocalDateTime.of(2024, 4, 5, 12, 0);

        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .startDate(expectedStartDate)
            .build();

        ProjectRequest projectRequest = mapStructMapper.toRequest(expectedProject);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        mockMvc.perform(post("/projects")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
            .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").exists())      // TODO: replace exists method call with value method call with actual message value
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isNotBlank());
    }

    @Test
    @DisplayName("Should return OK status when no exception was thrown when calling the updateProject endpoint")
    void updateProject_NoExceptionThrown_OkResponse() throws Exception {
        String givenProjectKey = "FPC";
        ProjectUpdateRequest updateRequest = ProjectUpdateRequest.builder()
            .projectKey("FPCE")
            .name("First Project Created Ever")
            .description("First Project Created Ever Description")
            .startDate(NOW.toLocalDateTime())
            .targetEndDate(NOW.toLocalDateTime().plusYears(2))
            .actualEndDate(NOW.toLocalDateTime().plusYears(2))
            .projectManagerId("PM_00020")
            .build();
        
        mockMvc.perform(put("/projects/{projectKey}", givenProjectKey)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk());

        verify(projectService).updateProject(eq(givenProjectKey), projectRequestCaptor.capture());

        ProjectUpdateRequest actualUpdateRequest = projectRequestCaptor.getValue();

        assertThat(actualUpdateRequest).isEqualTo(updateRequest);
    }

    @Test
    @DisplayName("Should return NOT FOUND status when ProjectNotFoundException was thrown when calling the updateProject endpoint")
    void updateProject_ProjectNotFoundExceptionThrown_OkResponse() throws Exception {
        String givenProjectKey = "FPC";
        ProjectUpdateRequest updateRequest = ProjectUpdateRequest.builder()
            .projectKey("FPCE")
            .name("First Project Created Ever")
            .description("First Project Created Ever Description")
            .startDate(NOW.toLocalDateTime())
            .targetEndDate(NOW.toLocalDateTime().plusYears(2))
            .actualEndDate(NOW.toLocalDateTime().plusYears(2))
            .projectManagerId("PM_00020")
            .build();

        doThrow(new ProjectNotFoundException(givenProjectKey)).when(projectService).updateProject(givenProjectKey, updateRequest);
        
        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        mockMvc.perform(put("/projects/{projectKey}", givenProjectKey)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.status").value(NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value(formattedString(PROJECT_WITH_ID_NOT_FOUND, givenProjectKey)))
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ProjectNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(PROJECT_WITH_ID_NOT_FOUND, givenProjectKey)));

        verify(projectService).updateProject(eq(givenProjectKey), projectRequestCaptor.capture());

        ProjectUpdateRequest actualUpdateRequest = projectRequestCaptor.getValue();

        assertThat(actualUpdateRequest).isEqualTo(updateRequest);
    }

    @Test
    @DisplayName("Should return OK status when no exception was thrown when calling the assignUserToProject endpoint")
    void assignUserToProject_NoExceptionThrown_OkResponse() throws Exception {
        String givenProjectKey = "PROJECT1";
        String givenUserId = "JC_12345";

        mockMvc.perform(post("/projects/{projectKey}/assignUser/{userId}", givenProjectKey, givenUserId))
            .andExpect(status().isOk());

        verify(projectService).assignUserOnProject(givenProjectKey, givenUserId);
    }

    @Test
    @DisplayName("Should return NOT FOUND status when ProjectNotFoundException was thrown when calling the assignUserToProject endpoint")
    void assignUserToProject_ProjectNotFoundExceptionThrown_NotFoundResponse() throws Exception {
        String givenProjectKey = "PROJECT1";
        String givenUserId = "JC_12345";

        doThrow(new ProjectNotFoundException(givenProjectKey)).when(projectService).assignUserOnProject(givenProjectKey, givenUserId);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        mockMvc.perform(post("/projects/{projectKey}/assignUser/{userId}", givenProjectKey, givenUserId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.status").value(NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value(formattedString(PROJECT_WITH_ID_NOT_FOUND, givenProjectKey)))
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ProjectNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(PROJECT_WITH_ID_NOT_FOUND, givenProjectKey)));

        verify(projectService).assignUserOnProject(givenProjectKey, givenUserId);
    }

    @Test
    @DisplayName("Should return NOT FOUND status when UserIdNotFoundException was thrown when calling the assignUserToProject endpoint")
    void assignUserToProject_UserIdNotFoundExceptionThrown_NotFoundResponse() throws Exception {
        String givenProjectKey = "PROJECT1";
        String givenUserId = "JC_12345";

        doThrow(new UserIdNotFoundException(givenUserId)).when(projectService).assignUserOnProject(givenProjectKey, givenUserId);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        mockMvc.perform(post("/projects/{projectKey}/assignUser/{userId}", givenProjectKey, givenUserId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.status").value(NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value(formattedString(USER_WITH_ID_NOT_FOUND, givenUserId)))
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(USER_WITH_ID_NOT_FOUND, givenUserId)));

        verify(projectService).assignUserOnProject(givenProjectKey, givenUserId);
    }

    @Test
    @DisplayName("Should return OK status when no exception was thrown when calling the assignUsersToProject endpoint")
    void assignUsersToProject_NoExceptionThrown_OkResponse() throws Exception {
        String givenProjectKey = "PROJECT1";
        String firstGivenUserId = "JC_12345";
        String secondGivenUserId = "JD_12346";

        Set<String> usersIdsToBeAssigned = Set.of(
            firstGivenUserId,
            secondGivenUserId
        );

        mockMvc.perform(post("/projects/{projectKey}/assignUsers", givenProjectKey)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usersIdsToBeAssigned)))
            .andExpect(status().isOk());

        verify(projectService).assignUsersOnProject(givenProjectKey, usersIdsToBeAssigned);
    }

    @Test
    @DisplayName("Should return NOT FOUND status when ProjectNotFoundException was thrown when calling the assignUsersToProject endpoint")
    void assignUsersToProject_ProjectNotFoundExceptionThrown_NotFoundResponse() throws Exception {
        String givenProjectKey = "PROJECT1";
        String firstGivenUserId = "JC_12345";
        String secondGivenUserId = "JD_12346";

        Set<String> usersIdsToBeAssigned = Set.of(
            firstGivenUserId,
            secondGivenUserId
        );

        doThrow(new ProjectNotFoundException(givenProjectKey)).when(projectService).assignUsersOnProject(givenProjectKey, usersIdsToBeAssigned);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        mockMvc.perform(post("/projects/{projectKey}/assignUsers", givenProjectKey)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usersIdsToBeAssigned)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.status").value(NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value(formattedString(PROJECT_WITH_ID_NOT_FOUND, givenProjectKey)))
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ProjectNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(PROJECT_WITH_ID_NOT_FOUND, givenProjectKey)));

        verify(projectService).assignUsersOnProject(givenProjectKey, usersIdsToBeAssigned);
    }

    @Test
    @DisplayName("Should return NOT FOUND status when UserIdNotFoundException was thrown when calling the assignUsersToProject endpoint")
    void assignUsersToProject_UserIdNotFoundExceptionThrown_NotFoundResponse() throws Exception {
        String givenProjectKey = "PROJECT1";
        String firstGivenUserId = "JC_12345";
        String secondGivenUserId = "JD_12346";

        Set<String> usersIdsToBeAssigned = Set.of(
            firstGivenUserId,
            secondGivenUserId
        );

        doThrow(new UserIdNotFoundException(firstGivenUserId)).when(projectService).assignUsersOnProject(givenProjectKey, usersIdsToBeAssigned);

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        mockMvc.perform(post("/projects/{projectKey}/assignUsers", givenProjectKey)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usersIdsToBeAssigned)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.timestamp").value(NOW.format(ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .andExpect(jsonPath("$.code").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.status").value(NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value(formattedString(USER_WITH_ID_NOT_FOUND, firstGivenUserId)))
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(USER_WITH_ID_NOT_FOUND, firstGivenUserId)));

        verify(projectService).assignUsersOnProject(givenProjectKey, usersIdsToBeAssigned);
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
