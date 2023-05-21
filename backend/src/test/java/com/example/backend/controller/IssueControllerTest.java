package com.example.backend.controller;

import com.example.backend.dto.request.IssueRequest;
import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.enums.IssueStatus;
import com.example.backend.exception.issue.IssueAlreadyCreatedException;
import com.example.backend.exception.issue.IssueIdNotFoundException;
import com.example.backend.exception.issue.IssueStatusInvalidTransitionException;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.exception.user.UserEmailNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.IssueService;
import com.example.backend.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Profile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.example.backend.enums.IssuePriority.LOW;
import static com.example.backend.enums.IssueStatus.FIXED;
import static com.example.backend.enums.IssueStatus.NEW;
import static com.example.backend.enums.IssueStatus.NOT_A_BUG;
import static com.example.backend.enums.IssueStatus.OPEN;
import static com.example.backend.enums.IssueStatus.PENDING_RETEST;
import static com.example.backend.enums.IssueStatus.REJECTED;
import static com.example.backend.enums.IssueStatus.REOPENED;
import static com.example.backend.enums.IssueStatus.RETEST;
import static com.example.backend.enums.IssueStatus.VERIFIED;
import static com.example.backend.enums.IssueStatus.ASSIGNED;
import static com.example.backend.enums.IssueStatus.CLOSED;
import static com.example.backend.enums.IssueStatus.DEFERRED;
import static com.example.backend.enums.IssueStatus.DUPLICATE;
import static com.example.backend.util.ExceptionUtilities.ISSUE_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.ISSUE_ALREADY_CREATED;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_EMAIL_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.PROJECT_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.ISSUE_STATUS_INVALID_TRANSITION;

@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = IssueController.class, useDefaultFilters = false, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(IssueController.class)
@ComponentScan({"com.example.backend.mapper"})
@WithMockUser(username = "test.user@domain.com")
class IssueControllerTest {

    @Autowired
    private MapStructMapper mapStructMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IssueService issueService;

    @MockBean
    private Clock clock;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<IssueEntity> issueCaptor;

    @Captor
    private ArgumentCaptor<String> issueIdCaptor;

    @Captor
    private ArgumentCaptor<String> projectKeyCaptor;

    @Captor
    private ArgumentCaptor<String> developerIdCaptor;

    @Captor
    private ArgumentCaptor<String> userEmailCaptor;

    @Captor
    private ArgumentCaptor<IssueStatus> issueStatusCaptor;

    @Test
    @DisplayName("Should return OK Response when there are issues to return when calling the getAllIssues endpoint")
    void getAllIssues_ExistingIssues_OkResponse() throws Exception {
        IssueEntity firstExpectedIssue = IssueEntity.builder()
            .issueId("FI_00001")
            .title("Title")
            .description("Issue description")
            .reproducingSteps("Some steps to reproduce the issue")
            .environment("Environment")
            .version("v1.0")
            .status(NEW)
            .priority(LOW)
            .build();

        IssueEntity secondExpectedIssue = IssueEntity.builder()
            .issueId("SI_00001")
            .title("Title")
            .description("Issue description")
            .reproducingSteps("Some steps to reproduce the issue")
            .environment("Environment")
            .version("v1.0")
            .status(NEW)
            .priority(LOW)
            .build();

        IssueFullResponse firstExpectedIssueResponse = mapStructMapper.toResponse(firstExpectedIssue);
        IssueFullResponse secondExpectedIssueResponse = mapStructMapper.toResponse(secondExpectedIssue);

        List<IssueEntity> expectedIssues = List.of(
            firstExpectedIssue,
            secondExpectedIssue
        );

        when(issueService.getAllIssues()).thenReturn(expectedIssues);

        List<IssueFullResponse> expectedIssuesResponses = List.of(
            firstExpectedIssueResponse,
            secondExpectedIssueResponse
        );

        mockMvc.perform(get("/issues"))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedIssuesResponses)));
    }

    @Test
    @DisplayName("Should return OK Response when there no are issues to return when calling the getAllIssues endpoint")
    void getAllIssues_NoIssues_OkResponse() throws Exception {
        List<IssueEntity> expectedIssues = List.of();

        when(issueService.getAllIssues()).thenReturn(expectedIssues);

        List<IssueFullResponse> expectedIssuesResponses = List.of();

        mockMvc.perform(get("/issues"))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedIssuesResponses)));
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the getIssue endpoint")
    void getIssue_NoExceptionThrown_OkResponse() throws Exception {
        IssueEntity issue = IssueEntity.builder()
            .issueId("FI_00001")
            .title("Title")
            .description("Issue description")
            .reproducingSteps("Some steps to reproduce the issue")
            .environment("Environment")
            .version("v1.0")
            .status(NEW)
            .priority(LOW)
            .build();

        IssueFullResponse expectedIssueResponse = mapStructMapper.toResponse(issue);

        when(issueService.getIssueByIssueId("00001")).thenReturn(issue);

        mockMvc.perform(get("/issues/{issueId}", "00001"))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedIssueResponse)));
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the getIssue endpoint")
    void getIssue_IssueIdNotFoundExceptionThrown_ResolvedExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00005";

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).getIssueByIssueId(expectedIssueId);

        mockMvc.perform(get("/issues/{issueId}", expectedIssueId))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId));
    }

    @Test
    @DisplayName("Should return CREATED Response when no exception was thrown when calling the createIssue endpoint")
    void createIssue_NoExceptionThrown_CreatedResponse() throws Exception {
        String expectedProjectKey = "FP_00001";

        IssueEntity expectedIssue = IssueEntity.builder()
            .issueId("FI_00001")
            .title("Title")
            .description("Issue description")
            .reproducingSteps("Some steps to reproduce the issue")
            .environment("Environment")
            .version("v1.0")
            .priority(LOW)
            .build();

        IssueRequest issueRequest = mapStructMapper.toRequest(expectedIssue);

        mockMvc.perform(post("/issues/createOnProject/{projectKey}", expectedProjectKey)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueRequest)))
            .andExpect(status().isCreated());

        verify(issueService).saveIssue(
            issueCaptor.capture(),
            projectKeyCaptor.capture(),
            userEmailCaptor.capture()
        );

        IssueEntity actualIssue = issueCaptor.getValue();
        String actualProjectKey = projectKeyCaptor.getValue();
        String actualUserEmail = userEmailCaptor.getValue();

        assertThat(actualIssue.getIssueId()).isEqualTo(expectedIssue.getIssueId());
        assertThat(actualIssue.getTitle()).isEqualTo(expectedIssue.getTitle());
        assertThat(actualIssue.getDescription()).isEqualTo(expectedIssue.getDescription());
        assertThat(actualIssue.getReproducingSteps()).isEqualTo(expectedIssue.getReproducingSteps());
        assertThat(actualIssue.getEnvironment()).isEqualTo(expectedIssue.getEnvironment());
        assertThat(actualIssue.getVersion()).isEqualTo(expectedIssue.getVersion());
        assertThat(actualIssue.getPriority()).isEqualTo(expectedIssue.getPriority());

        assertThat(actualProjectKey).isEqualTo(expectedProjectKey);

        assertThat(actualUserEmail).isEqualTo("test.user@domain.com");
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when ProjectNotFoundException was thrown when calling the createIssue endpopint")
    void createIssue_ProjectNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedProjectKey = "FP_00001";

        IssueEntity expectedIssue = IssueEntity.builder()
            .issueId("FI_00001")
            .title("Title")
            .description("Issue description")
            .reproducingSteps("Some steps to reproduce the issue")
            .environment("Environment")
            .version("v1.0")
            .priority(LOW)
            .build();

        IssueRequest issueRequest = IssueRequest.builder()
            .issueId("FI_00001")
            .title("Title")
            .description("Issue description")
            .reproducingSteps("Some steps to reproduce the issue")
            .environment("Environment")
            .version("v1.0")
            .priority("LOW")
            .build();

        doThrow(new ProjectNotFoundException(expectedProjectKey)).when(issueService).saveIssue(any(), eq(expectedProjectKey), eq("test.user@domain.com"));

        mockMvc.perform(post("/issues/createOnProject/{projectKey}", expectedProjectKey)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueRequest)))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ProjectNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(PROJECT_WITH_ID_NOT_FOUND, expectedProjectKey)));

        verify(issueService).saveIssue(
            issueCaptor.capture(),
            projectKeyCaptor.capture(),
            userEmailCaptor.capture()
        );

        IssueEntity actualIssue = issueCaptor.getValue();
        String actualProjectKey = projectKeyCaptor.getValue();
        String actualUserEmail = userEmailCaptor.getValue();

        assertThat(actualIssue.getIssueId()).isEqualTo(expectedIssue.getIssueId());
        assertThat(actualIssue.getTitle()).isEqualTo(expectedIssue.getTitle());
        assertThat(actualIssue.getDescription()).isEqualTo(expectedIssue.getDescription());
        assertThat(actualIssue.getReproducingSteps()).isEqualTo(expectedIssue.getReproducingSteps());
        assertThat(actualIssue.getEnvironment()).isEqualTo(expectedIssue.getEnvironment());
        assertThat(actualIssue.getVersion()).isEqualTo(expectedIssue.getVersion());
        assertThat(actualIssue.getPriority()).isEqualTo(expectedIssue.getPriority());

        assertThat(actualProjectKey).isEqualTo(expectedProjectKey);

        assertThat(actualUserEmail).isEqualTo("test.user@domain.com");
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when UserEmailNotFoundException was thrown when calling the createIssue endpopint")
    void createIssue_UserEmailNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedProjectKey = "FP_00001";

        IssueEntity expectedIssue = IssueEntity.builder()
            .issueId("FI_00001")
            .title("Title")
            .description("Issue description")
            .reproducingSteps("Some steps to reproduce the issue")
            .environment("Environment")
            .version("v1.0")
            .priority(LOW)
            .build();

        IssueRequest issueRequest = IssueRequest.builder()
            .issueId("FI_00001")
            .title("Title")
            .description("Issue description")
            .reproducingSteps("Some steps to reproduce the issue")
            .environment("Environment")
            .version("v1.0")
            .priority("LOW")
            .build();

        doThrow(new UserEmailNotFoundException("test.user@domain.com")).when(issueService).saveIssue(any(), eq(expectedProjectKey), eq("test.user@domain.com"));

        mockMvc.perform(post("/issues/createOnProject/{projectKey}", expectedProjectKey)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issueRequest)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserEmailNotFoundException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(USER_WITH_EMAIL_NOT_FOUND, "test.user@domain.com")));
    
        verify(issueService).saveIssue(
            issueCaptor.capture(),
            projectKeyCaptor.capture(),
            userEmailCaptor.capture()
        );

        IssueEntity actualIssue = issueCaptor.getValue();
        String actualProjectKey = projectKeyCaptor.getValue();
        String actualUserEmail = userEmailCaptor.getValue();

        assertThat(actualIssue.getIssueId()).isEqualTo(expectedIssue.getIssueId());
        assertThat(actualIssue.getTitle()).isEqualTo(expectedIssue.getTitle());
        assertThat(actualIssue.getDescription()).isEqualTo(expectedIssue.getDescription());
        assertThat(actualIssue.getReproducingSteps()).isEqualTo(expectedIssue.getReproducingSteps());
        assertThat(actualIssue.getEnvironment()).isEqualTo(expectedIssue.getEnvironment());
        assertThat(actualIssue.getVersion()).isEqualTo(expectedIssue.getVersion());
        assertThat(actualIssue.getPriority()).isEqualTo(expectedIssue.getPriority());

        assertThat(actualProjectKey).isEqualTo(expectedProjectKey);

        assertThat(actualUserEmail).isEqualTo("test.user@domain.com");
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueAlreadyCreatedException was thrown when calling the createIssue endpopint")
    void createIssue_IssueAlreadyCreatedExceptionThrown_ResolveExceptionAndBadRequestResponse() throws Exception {
        String expectedProjectKey = "FP_00001";
        String expectedIssueId = "FI_00001";

        IssueEntity expectedIssue = IssueEntity.builder()
            .issueId("FI_00001")
            .title("Title")
            .description("Issue description")
            .reproducingSteps("Some steps to reproduce the issue")
            .environment("Environment")
            .version("v1.0")
            .priority(LOW)
            .build();

        IssueRequest issueRequest = IssueRequest.builder()
            .issueId("FI_00001")
            .title("Title")
            .description("Issue description")
            .reproducingSteps("Some steps to reproduce the issue")
            .environment("Environment")
            .version("v1.0")
            .priority("LOW")
            .build();

        doThrow(new IssueAlreadyCreatedException(expectedIssueId)).when(issueService).saveIssue(any(), eq(expectedProjectKey), eq("test.user@domain.com"));

        mockMvc.perform(post("/issues/createOnProject/{projectKey}", expectedProjectKey)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issueRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueAlreadyCreatedException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_ALREADY_CREATED, expectedIssueId)));
    
        verify(issueService).saveIssue(
            issueCaptor.capture(),
            projectKeyCaptor.capture(),
            userEmailCaptor.capture()
        );

        IssueEntity actualIssue = issueCaptor.getValue();
        String actualProjectKey = projectKeyCaptor.getValue();
        String actualUserEmail = userEmailCaptor.getValue();

        assertThat(actualIssue.getIssueId()).isEqualTo(expectedIssue.getIssueId());
        assertThat(actualIssue.getTitle()).isEqualTo(expectedIssue.getTitle());
        assertThat(actualIssue.getDescription()).isEqualTo(expectedIssue.getDescription());
        assertThat(actualIssue.getReproducingSteps()).isEqualTo(expectedIssue.getReproducingSteps());
        assertThat(actualIssue.getEnvironment()).isEqualTo(expectedIssue.getEnvironment());
        assertThat(actualIssue.getVersion()).isEqualTo(expectedIssue.getVersion());
        assertThat(actualIssue.getPriority()).isEqualTo(expectedIssue.getPriority());

        assertThat(actualProjectKey).isEqualTo(expectedProjectKey);

        assertThat(actualUserEmail).isEqualTo("test.user@domain.com");
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the assignToDeveloper endpoint")
    void assignToDeveloper_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        String expectedDeveloperId = "FU_00001";
        IssueStatus expectedStatus = ASSIGNED;

		mockMvc.perform(put("/issues/{issueId}/assignToDeveloper/{developerId}", expectedIssueId, expectedDeveloperId))
			.andExpect(status().isOk());

        verify(issueService).assignToUser(
            issueIdCaptor.capture(),
            developerIdCaptor.capture()
        );

        String actualIssueIdAssignToUser = issueIdCaptor.getValue();
        String actualDeveloperId = developerIdCaptor.getValue();

        assertThat(actualIssueIdAssignToUser).isEqualTo(expectedIssueId);
        assertThat(actualDeveloperId).isEqualTo(expectedDeveloperId);

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueIdChangeIssueStatus = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueIdChangeIssueStatus).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown in assignToUser when calling the assignToDeveloper endpoint")
    void assignToDeveloper_IssueIdNotFoundExceptionThrownInAssignToUser_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        String expectedDeveloperId = "FU_00001";

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).assignToUser(expectedIssueId, expectedDeveloperId);

        mockMvc.perform(put("/issues/{issueId}/assignToDeveloper/{developerId}", expectedIssueId, expectedDeveloperId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).assignToUser(
            issueIdCaptor.capture(),
            developerIdCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        String actualDeveloperId = developerIdCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualDeveloperId).isEqualTo(expectedDeveloperId);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when UserIdNotFoundException was thrown when calling the assignToDeveloper endpoint")
    void assignToDeveloper_UserIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        String expectedDeveloperId = "FU_00001";

        doThrow(new UserIdNotFoundException(expectedDeveloperId)).when(issueService).assignToUser(expectedIssueId, expectedDeveloperId);

        mockMvc.perform(put("/issues/{issueId}/assignToDeveloper/{developerId}", expectedIssueId, expectedDeveloperId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(USER_WITH_ID_NOT_FOUND, expectedDeveloperId)));

        verify(issueService).assignToUser(
            issueIdCaptor.capture(),
            developerIdCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        String actualDeveloperId = developerIdCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualDeveloperId).isEqualTo(expectedDeveloperId);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown in changeIssueStatus when calling the assignToDeveloper endpoint")
    void assignToDeveloper_IssueIdNotFoundExceptionThrownInChangeIssueStatus_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        String expectedDeveloperId = "FU_00001";
        IssueStatus expectedStatus = ASSIGNED;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

        mockMvc.perform(put("/issues/{issueId}/assignToDeveloper/{developerId}", expectedIssueId, expectedDeveloperId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).assignToUser(
            issueIdCaptor.capture(),
            developerIdCaptor.capture()
        );

        String actualIssueIdAssignToUser = issueIdCaptor.getValue();
        String actualDeveloperId = developerIdCaptor.getValue();

        assertThat(actualIssueIdAssignToUser).isEqualTo(expectedIssueId);
        assertThat(actualDeveloperId).isEqualTo(expectedDeveloperId);
        

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueIdChangeIssueStatus = issueIdCaptor.getValue();
        IssueStatus actualIsueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueIdChangeIssueStatus).isEqualTo(expectedIssueId);
        assertThat(actualIsueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the assignToDeveloper endpoint")
    void assignToDeveloper_IssueStatusInvalidTransitionException_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        String expectedDeveloperId = "FU_00001";
        IssueStatus expectedStatus = ASSIGNED;
        IssueStatus currentStatus = OPEN;

        doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

        mockMvc.perform(put("/issues/{issueId}/assignToDeveloper/{developerId}", expectedIssueId, expectedDeveloperId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).assignToUser(
            issueIdCaptor.capture(),
            developerIdCaptor.capture()
        );

        String actualIssueIdAssignToUser = issueIdCaptor.getValue();
        String actualDeveloperId = developerIdCaptor.getValue();

        assertThat(actualIssueIdAssignToUser).isEqualTo(expectedIssueId);
        assertThat(actualDeveloperId).isEqualTo(expectedDeveloperId);
        

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueIdChangeIssueStatus = issueIdCaptor.getValue();
        IssueStatus actualIsueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueIdChangeIssueStatus).isEqualTo(expectedIssueId);
        assertThat(actualIsueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the open endpoint")
    void open_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = OPEN;

		mockMvc.perform(put("/issues/{issueId}/open", expectedIssueId))
			.andExpect(status().isOk());

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the open endpoint")
    void open_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = OPEN;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/open", expectedIssueId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the open endpoint")
    void open_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = OPEN;
        IssueStatus currentStatus = NEW;

		doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/open", expectedIssueId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the fix endpoint")
    void fix_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = FIXED;

		mockMvc.perform(put("/issues/{issueId}/fix", expectedIssueId))
			.andExpect(status().isOk());

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the fix endpoint")
    void fix_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = FIXED;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/fix", expectedIssueId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the fix endpoint")
    void fix_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = FIXED;
        IssueStatus currentStatus = ASSIGNED;

		doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/fix", expectedIssueId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the sendToRetest endpoint")
    void sendToRetest_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = PENDING_RETEST;

		mockMvc.perform(put("/issues/{issueId}/sendToRetest", expectedIssueId))
			.andExpect(status().isOk());

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the sendToRetest endpoint")
    void sendToRetest_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = PENDING_RETEST;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/sendToRetest", expectedIssueId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the sendToRetest endpoint")
    void sendToRetest_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = PENDING_RETEST;
        IssueStatus currentStatus = OPEN;

		doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/sendToRetest", expectedIssueId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the retest endpoint")
    void retest_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = RETEST;

		mockMvc.perform(put("/issues/{issueId}/retest", expectedIssueId))
			.andExpect(status().isOk());

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the retest endpoint")
    void retest_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = RETEST;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/retest", expectedIssueId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the retest endpoint")
    void retest_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = RETEST;
        IssueStatus currentStatus = FIXED;

		doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/retest", expectedIssueId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the reopen endpoint")
    void reopen_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = REOPENED;

		mockMvc.perform(put("/issues/{issueId}/reopen", expectedIssueId))
			.andExpect(status().isOk());

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the reopen endpoint")
    void reopen_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = REOPENED;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/reopen", expectedIssueId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the reopen endpoint")
    void reopen_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = REOPENED;
        IssueStatus currentStatus = RETEST;

		doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/reopen", expectedIssueId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the verify endpoint")
    void verify_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = VERIFIED;

		mockMvc.perform(put("/issues/{issueId}/verify", expectedIssueId))
			.andExpect(status().isOk());

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the verify endpoint")
    void verify_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = VERIFIED;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/verify", expectedIssueId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the verify endpoint")
    void verify_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = VERIFIED;
        IssueStatus currentStatus = PENDING_RETEST;

		doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/verify", expectedIssueId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the closeByDeveloper endpoint")
    void close_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        String expectedDeveloperId = "FD_00001";
        IssueStatus expectedStatus = CLOSED;

		mockMvc.perform(put("/issues/{issueId}/closeByDeveloper/{developerId}", expectedIssueId, expectedDeveloperId))
			.andExpect(status().isOk());

        verify(issueService).closeByUser(
            issueIdCaptor.capture(),
            developerIdCaptor.capture()
        );

        String actualIssueIdCloseByUser = issueIdCaptor.getValue();
        String actualDeveloperId = developerIdCaptor.getValue();

        assertThat(actualIssueIdCloseByUser).isEqualTo(expectedIssueId);
        assertThat(actualDeveloperId).isEqualTo(expectedDeveloperId);

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueIdChangeStatus = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueIdChangeStatus).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the closeByDeveloper endpoint")
    void close_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        String expectedDeveloperId = "FD_00001";
        IssueStatus expectedStatus = CLOSED;
        IssueStatus currentStatus = RETEST;

        doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).closeByUser(expectedIssueId, expectedDeveloperId);

		mockMvc.perform(put("/issues/{issueId}/closeByDeveloper/{developerId}", expectedIssueId, expectedDeveloperId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).closeByUser(
            issueIdCaptor.capture(),
            developerIdCaptor.capture()
        );

        String actualIssueIdCloseByUser = issueIdCaptor.getValue();
        String actualDeveloperId = developerIdCaptor.getValue();

        assertThat(actualIssueIdCloseByUser).isEqualTo(expectedIssueId);
        assertThat(actualDeveloperId).isEqualTo(expectedDeveloperId);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown in changeIssueStatus when calling the closeByDeveloper endpoint")
    void close_IssueIdNotFoundExceptionThrownInChangeIssueStatus_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        String expectedDeveloperId = "FD_00001";
        IssueStatus expectedStatus = CLOSED;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/closeByDeveloper/{developerId}", expectedIssueId, expectedDeveloperId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).closeByUser(
            issueIdCaptor.capture(),
            developerIdCaptor.capture()
        );

        String actualIssueIdCloseByUser = issueIdCaptor.getValue();
        String actualDeveloperId = developerIdCaptor.getValue();

        assertThat(actualIssueIdCloseByUser).isEqualTo(expectedIssueId);
        assertThat(actualDeveloperId).isEqualTo(expectedDeveloperId);

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when UserIdNotFoundException was thrown when calling the closeByDeveloper endpoint")
    void close_UserIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        String expectedDeveloperId = "FD_00001";

        doThrow(new UserIdNotFoundException(expectedDeveloperId)).when(issueService).closeByUser(expectedIssueId, expectedDeveloperId);

		mockMvc.perform(put("/issues/{issueId}/closeByDeveloper/{developerId}", expectedIssueId, expectedDeveloperId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(USER_WITH_ID_NOT_FOUND, expectedDeveloperId)));

        verify(issueService).closeByUser(
            issueIdCaptor.capture(),
            developerIdCaptor.capture()
        );

        String actualIssueIdCloseByUser = issueIdCaptor.getValue();
        String actualDeveloperId = developerIdCaptor.getValue();

        assertThat(actualIssueIdCloseByUser).isEqualTo(expectedIssueId);
        assertThat(actualDeveloperId).isEqualTo(expectedDeveloperId);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown in closeByUser when calling the closeByDeveloper endpoint")
    void close_IssueIdNotFoundExceptionThrownInCloseByUser_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        String expectedDeveloperId = "FD_00001";

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).closeByUser(expectedIssueId, expectedDeveloperId);

		mockMvc.perform(put("/issues/{issueId}/closeByDeveloper/{developerId}", expectedIssueId, expectedDeveloperId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).closeByUser(
            issueIdCaptor.capture(),
            developerIdCaptor.capture()
        );

        String actualIssueIdCloseByUser = issueIdCaptor.getValue();
        String actualDeveloperId = developerIdCaptor.getValue();

        assertThat(actualIssueIdCloseByUser).isEqualTo(expectedIssueId);
        assertThat(actualDeveloperId).isEqualTo(expectedDeveloperId);
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the duplicate endpoint")
    void duplicate_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = DUPLICATE;

		mockMvc.perform(put("/issues/{issueId}/duplicate", expectedIssueId))
			.andExpect(status().isOk());

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the duplicate endpoint")
    void duplicate_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = DUPLICATE;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/duplicate", expectedIssueId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the duplicate endpoint")
    void duplicate_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = DUPLICATE;
        IssueStatus currentStatus = PENDING_RETEST;

		doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/duplicate", expectedIssueId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the reject endpoint")
    void reject_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = REJECTED;

		mockMvc.perform(put("/issues/{issueId}/reject", expectedIssueId))
			.andExpect(status().isOk());

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the reject endpoint")
    void reject_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = REJECTED;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/reject", expectedIssueId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the reject endpoint")
    void reject_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = REJECTED;
        IssueStatus currentStatus = PENDING_RETEST;

		doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/reject", expectedIssueId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the defer endpoint")
    void defer_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = DEFERRED;

		mockMvc.perform(put("/issues/{issueId}/defer", expectedIssueId))
			.andExpect(status().isOk());

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the defer endpoint")
    void defer_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = DEFERRED;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/defer", expectedIssueId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the defer endpoint")
    void defer_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = DEFERRED;
        IssueStatus currentStatus = PENDING_RETEST;

		doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/defer", expectedIssueId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the notABug endpoint")
    void notABug_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = NOT_A_BUG;

		mockMvc.perform(put("/issues/{issueId}/notABug", expectedIssueId))
			.andExpect(status().isOk());

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the notABug endpoint")
    void notABug_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = NOT_A_BUG;

        doThrow(new IssueIdNotFoundException(expectedIssueId)).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/notABug", expectedIssueId))
			.andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_WITH_ID_NOT_FOUND, expectedIssueId)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the notABug endpoint")
    void notABug_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = NOT_A_BUG;
        IssueStatus currentStatus = PENDING_RETEST;

		doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/notABug", expectedIssueId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_STATUS_INVALID_TRANSITION, currentStatus, expectedStatus)));

        verify(issueService).changeIssueStatus(
            issueIdCaptor.capture(),
            issueStatusCaptor.capture()
        );

        String actualIssueId = issueIdCaptor.getValue();
        IssueStatus actualIssueStatus = issueStatusCaptor.getValue();

        assertThat(actualIssueId).isEqualTo(expectedIssueId);
        assertThat(actualIssueStatus).isEqualTo(expectedStatus);
    }
}