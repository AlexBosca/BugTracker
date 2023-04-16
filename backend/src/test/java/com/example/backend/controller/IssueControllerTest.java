package com.example.backend.controller;

import com.example.backend.dto.request.IssueRequest;
import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.enums.IssueStatus;
import com.example.backend.exception.issue.IssueAlreadyCreatedException;
import com.example.backend.exception.issue.IssueIdNotFoundException;
import com.example.backend.exception.issue.IssueStatusInvalidTransitionException;
import com.example.backend.exception.project.ProjectIdNotFoundException;
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
import static com.example.backend.enums.IssueStatus.OPEN;
import static com.example.backend.enums.IssueStatus.ASSIGNED;

import static com.example.backend.util.ExceptionUtilities.ISSUE_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.ISSUE_ALREADY_CREATED;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_EMAIL_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.PROJECT_WITH_ID_NOT_FOUND;

@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = IssueController.class, useDefaultFilters = false, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(IssueController.class)
@ComponentScan({"com.example.backend.mapper"})
@WithMockUser(username = "test.user@domain.com")
public class IssueControllerTest {

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
    private ArgumentCaptor<String> projectIdCaptor;

    @Captor
    private ArgumentCaptor<String> developerIdCaptor;

    @Captor
    private ArgumentCaptor<String> userEmailCaptor;

    @Captor
    private ArgumentCaptor<IssueStatus> issueStatusCaptor;

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
        String expectedProjectId = "FP_00001";

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

        mockMvc.perform(post("/issues/createOnProject/{projectId}", expectedProjectId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueRequest)))
            .andExpect(status().isCreated());

        verify(issueService).saveIssue(
            issueCaptor.capture(),
            projectIdCaptor.capture(),
            userEmailCaptor.capture()
        );

        IssueEntity actualIssue = issueCaptor.getValue();
        String actualProjectId = projectIdCaptor.getValue();
        String actualUserEmail = userEmailCaptor.getValue();

        assertThat(actualIssue.getIssueId()).isEqualTo(expectedIssue.getIssueId());
        assertThat(actualIssue.getTitle()).isEqualTo(expectedIssue.getTitle());
        assertThat(actualIssue.getDescription()).isEqualTo(expectedIssue.getDescription());
        assertThat(actualIssue.getReproducingSteps()).isEqualTo(expectedIssue.getReproducingSteps());
        assertThat(actualIssue.getEnvironment()).isEqualTo(expectedIssue.getEnvironment());
        assertThat(actualIssue.getVersion()).isEqualTo(expectedIssue.getVersion());
        assertThat(actualIssue.getPriority()).isEqualTo(expectedIssue.getPriority());

        assertThat(actualProjectId).isEqualTo(expectedProjectId);

        assertThat(actualUserEmail).isEqualTo("test.user@domain.com");
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when ProjectIdNotFoundException was thrown when calling the createIssue endpopint")
    void createIssue_ProjectIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedProjectId = "FP_00001";

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

        doThrow(new ProjectIdNotFoundException(expectedProjectId)).when(issueService).saveIssue(any(), eq(expectedProjectId), eq("test.user@domain.com"));

        mockMvc.perform(post("/issues/createOnProject/{projectId}", expectedProjectId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueRequest)))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ProjectIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(PROJECT_WITH_ID_NOT_FOUND, expectedProjectId)));

        verify(issueService).saveIssue(
            issueCaptor.capture(),
            projectIdCaptor.capture(),
            userEmailCaptor.capture()
        );

        IssueEntity actualIssue = issueCaptor.getValue();
        String actualProjectId = projectIdCaptor.getValue();
        String actualUserEmail = userEmailCaptor.getValue();

        assertThat(actualIssue.getIssueId()).isEqualTo(expectedIssue.getIssueId());
        assertThat(actualIssue.getTitle()).isEqualTo(expectedIssue.getTitle());
        assertThat(actualIssue.getDescription()).isEqualTo(expectedIssue.getDescription());
        assertThat(actualIssue.getReproducingSteps()).isEqualTo(expectedIssue.getReproducingSteps());
        assertThat(actualIssue.getEnvironment()).isEqualTo(expectedIssue.getEnvironment());
        assertThat(actualIssue.getVersion()).isEqualTo(expectedIssue.getVersion());
        assertThat(actualIssue.getPriority()).isEqualTo(expectedIssue.getPriority());

        assertThat(actualProjectId).isEqualTo(expectedProjectId);

        assertThat(actualUserEmail).isEqualTo("test.user@domain.com");
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when UserEmailNotFoundException was thrown when calling the createIssue endpopint")
    void createIssue_UserEmailNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedProjectId = "FP_00001";

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

        doThrow(new UserEmailNotFoundException("test.user@domain.com")).when(issueService).saveIssue(any(), eq(expectedProjectId), eq("test.user@domain.com"));

        mockMvc.perform(post("/issues/createOnProject/{projectId}", expectedProjectId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issueRequest)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserEmailNotFoundException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(USER_WITH_EMAIL_NOT_FOUND, "test.user@domain.com")));
    
        verify(issueService).saveIssue(
            issueCaptor.capture(),
            projectIdCaptor.capture(),
            userEmailCaptor.capture()
        );

        IssueEntity actualIssue = issueCaptor.getValue();
        String actualProjectId = projectIdCaptor.getValue();
        String actualUserEmail = userEmailCaptor.getValue();

        assertThat(actualIssue.getIssueId()).isEqualTo(expectedIssue.getIssueId());
        assertThat(actualIssue.getTitle()).isEqualTo(expectedIssue.getTitle());
        assertThat(actualIssue.getDescription()).isEqualTo(expectedIssue.getDescription());
        assertThat(actualIssue.getReproducingSteps()).isEqualTo(expectedIssue.getReproducingSteps());
        assertThat(actualIssue.getEnvironment()).isEqualTo(expectedIssue.getEnvironment());
        assertThat(actualIssue.getVersion()).isEqualTo(expectedIssue.getVersion());
        assertThat(actualIssue.getPriority()).isEqualTo(expectedIssue.getPriority());

        assertThat(actualProjectId).isEqualTo(expectedProjectId);

        assertThat(actualUserEmail).isEqualTo("test.user@domain.com");
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueAlreadyCreatedException was thrown when calling the createIssue endpopint")
    void createIssue_IssueAlreadyCreatedExceptionThrown_ResolveExceptionAndBadRequestResponse() throws Exception {
        String expectedProjectId = "FP_00001";
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

        doThrow(new IssueAlreadyCreatedException(expectedIssueId)).when(issueService).saveIssue(any(), eq(expectedProjectId), eq("test.user@domain.com"));

        mockMvc.perform(post("/issues/createOnProject/{projectId}", expectedProjectId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issueRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueAlreadyCreatedException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(ISSUE_ALREADY_CREATED, expectedIssueId)));
    
        verify(issueService).saveIssue(
            issueCaptor.capture(),
            projectIdCaptor.capture(),
            userEmailCaptor.capture()
        );

        IssueEntity actualIssue = issueCaptor.getValue();
        String actualProjectId = projectIdCaptor.getValue();
        String actualUserEmail = userEmailCaptor.getValue();

        assertThat(actualIssue.getIssueId()).isEqualTo(expectedIssue.getIssueId());
        assertThat(actualIssue.getTitle()).isEqualTo(expectedIssue.getTitle());
        assertThat(actualIssue.getDescription()).isEqualTo(expectedIssue.getDescription());
        assertThat(actualIssue.getReproducingSteps()).isEqualTo(expectedIssue.getReproducingSteps());
        assertThat(actualIssue.getEnvironment()).isEqualTo(expectedIssue.getEnvironment());
        assertThat(actualIssue.getVersion()).isEqualTo(expectedIssue.getVersion());
        assertThat(actualIssue.getPriority()).isEqualTo(expectedIssue.getPriority());

        assertThat(actualProjectId).isEqualTo(expectedProjectId);

        assertThat(actualUserEmail).isEqualTo("test.user@domain.com");
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the assignToDeveloper endpoint")
    void assignToDeveloper_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        String expectedDeveloperId = "FU_00001";

		mockMvc.perform(put("/issues/{issueId}/assignToDeveloper/{developerId}", expectedIssueId, expectedDeveloperId))
			.andExpect(status().isOk());

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
    @DisplayName("Should return NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown when calling the assignToDeveloper endpoint")
    void assignToDeveloper_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
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
        

        // Same thing for changeIssueStatus call:
        // verify(issueService).assignToUser(
        //     issueIdCaptor.capture(),
        //     developerIdCaptor.capture()
        // );

        // String actualIssueId = issueIdCaptor.getValue();
        // String actualDeveloperId = developerIdCaptor.getValue();

        // assertThat(actualIssueId).isEqualTo(expectedIssueId);
        // assertThat(actualDeveloperId).isEqualTo(expectedDeveloperId);
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
    @DisplayName("Should return OK Response when no exception was thrown when calling the open endpoint")
    void open_NoExceptionThrown_OkResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = IssueStatus.OPEN;

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

    @Disabled("Cannot throw IssueStatusInvalidTransitionException in changeIssueStatus method")
    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when IssueStatusInvalidTransitionException was thrown when calling the open endpoint")
    void open_IssueStatusInvalidTransitionExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        String expectedIssueId = "FI_00001";
        IssueStatus expectedStatus = FIXED;
        IssueStatus currentStatus = ASSIGNED;

		doThrow(new IssueStatusInvalidTransitionException(currentStatus.name(), expectedStatus.name())).when(issueService).changeIssueStatus(expectedIssueId, expectedStatus);

		mockMvc.perform(put("/issues/{issueId}/open", expectedIssueId))
			.andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IssueStatusInvalidTransitionException.class))
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
}