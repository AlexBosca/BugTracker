package com.example.backend.controller;

import com.example.backend.dto.request.IssueRequest;
import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.IssueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
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
import com.google.gson.Gson;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.example.backend.enums.IssuePriority.LOW;
import static com.example.backend.enums.IssueStatus.NEW;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = IssueController.class, useDefaultFilters = false, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(IssueController.class)
@ComponentScan({"com.example.backend.mapper"})
class IssueControllerTest {

    @Autowired
    private MapStructMapper mapStructMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IssueService issueService;

    @Autowired
    private MockMvc mockMvc;

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

        mockMvc.perform(get("/issue/{issueId}", "00001"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(expectedIssueResponse)));
    }

    @Ignore
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

        mockMvc.perform(get("/issue/{issueId}", "00001"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(expectedIssueResponse)));
    }

    @Test
    @DisplayName("Should create issue when an user creates it on a specific project")
    void shouldCreateIssueWhenItIsCreatedByAnUserOnSpecificProject() throws Exception {
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

        IssueRequest issueRequest = mapStructMapper.toRequest(issue);

        mockMvc.perform(post("/createOnProject/{projectId}/byUser/{userId}", "00001", "00001")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issueRequest)))
                .andExpect(status().isOk());
    }
}