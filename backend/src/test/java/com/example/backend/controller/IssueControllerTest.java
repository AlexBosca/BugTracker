package com.example.backend.controller;

import com.example.backend.dto.request.IssueRequest;
import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.issue.IssueIdNotFoundException;
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
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.example.backend.enums.IssuePriority.LOW;
import static com.example.backend.enums.IssueStatus.NEW;

import static com.example.backend.util.ExceptionUtilities.ISSUE_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_ID_NOT_FOUND;

@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = IssueController.class, useDefaultFilters = false, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(IssueController.class)
@ComponentScan({"com.example.backend.mapper"})
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
    private ArgumentCaptor<String> issueIdCaptor;

    @Captor
    private ArgumentCaptor<String> developerIdCaptor;

    @Disabled
    @Test
    @DisplayName("Should return a specific issue by its issueId when it exists")
    void shouldReturnIssueByIssueIdWhenExists() throws Exception {
        IssueEntity issue = IssueEntity.builder()
                .issueId("00001")
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

    @Disabled
    @Test
    @DisplayName("Should throw an IllegalStateException when issue with issueId doesn't exists")
    void shouldThrowExceptionWhenIssueWithIssueIdDoNotExists() throws Exception {
        IssueEntity issue = IssueEntity.builder()
                .issueId("00001")
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

    @Disabled
    @Test
    @DisplayName("Should create issue when an user creates it on a specific project")
    void shouldCreateIssueWhenItIsCreatedByAnUserOnSpecificProject() throws Exception {
        String givenEmail = "some.user@mail.com";
        String givenProjectId = "00001";

        UserEntity user = UserEntity.builder()
                .email("some.user@mail.com")
                .build();

        ProjectEntity project = ProjectEntity.builder()
                .projectId("00001").build();

        IssueEntity issue = IssueEntity.builder()
                .issueId("00001")
                .title("Title")
                .description("Issue description")
                .reproducingSteps("Some steps to reproduce the issue")
                .environment("Environment")
                .version("v1.0")
                .status(NEW)
                .priority(LOW)
                .build();

        // when(authentication.getName()).thenReturn(givenEmail);
        // when(issueService.findProject(givenProjectId)).thenReturn(project);
        // when(issueService.findUserByEmail(givenEmail)).thenReturn(user);

        IssueRequest issueRequest = mapStructMapper.toRequest(issue);

        mockMvc.perform(post("/issues/createOnProject/{projectId}", givenProjectId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issueRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return an OK Response when no exception was thrown")
    public void assignToDeveloper_NoExceptionThrown_OkResponse() throws Exception {
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
    @DisplayName("Should return a NOT FOUND Response and resolve exception when IssueIdNotFoundException was thrown")
    public void assignToDeveloper_IssueIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
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
    @DisplayName("Should return a NOT FOUND Response and resolve exception when UserIdNotFoundException was thrown")
    public void assignToDeveloper_UserIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
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
}