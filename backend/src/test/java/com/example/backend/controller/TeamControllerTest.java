package com.example.backend.controller;

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

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.TeamRequest;
import com.example.backend.dto.response.TeamFullResponse;
import com.example.backend.entity.TeamEntity;
import com.example.backend.exception.project.ProjectAlreadyCreatedException;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.exception.team.TeamAlreadyCreatedException;
import com.example.backend.exception.team.TeamIdNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.TeamService;
import com.example.backend.util.FilterCriteriaMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.example.backend.util.ExceptionUtilities.TEAM_ALREADY_CREATED;
import static com.example.backend.util.ExceptionUtilities.TEAM_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_ID_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = TeamController.class, useDefaultFilters = false, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(TeamController.class)
@ComponentScan({"com.example.backend.mapper"})
class TeamControllerTest {
    
    @Autowired
    private MapStructMapper mapStructMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeamService teamService;

    @MockBean
    private Clock clock;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<TeamEntity> teamCaptor;

    @Test
    @DisplayName("Should return OK Response when there are teams to return when calling the getAllTeams endpoint")
    void getAllTeams_ExistingTeams_OkResponse() throws Exception {
        TeamEntity firstExpectedTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .build();

        TeamEntity secondExpectedTeam = TeamEntity.builder()
            .teamId("TEAM2")
            .name("Second Team")
            .build();

        TeamFullResponse firstExpectedTeamResponse = mapStructMapper.toResponse(firstExpectedTeam);
        TeamFullResponse secondExpectedTeamResponse = mapStructMapper.toResponse(secondExpectedTeam);

        List<TeamEntity> expectedTeams = List.of(
            firstExpectedTeam,
            secondExpectedTeam
        );

        when(teamService.getAllTeams()).thenReturn(expectedTeams);

        List<TeamFullResponse> expectedProjectsResponses = List.of(
            firstExpectedTeamResponse,
            secondExpectedTeamResponse
        );

        mockMvc.perform(get("/teams"))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedProjectsResponses)));
    }

    @Test
    @DisplayName("Should return OK Response when there are no teams to return when calling the getAllTeams endpoint")
    void getAllTeams_NoTeams_OkResponse() throws Exception {
        List<TeamEntity> expectedTeams = List.of();

        when(teamService.getAllTeams()).thenReturn(expectedTeams);

        List<TeamFullResponse> expectedProjectsResponses = List.of();

        mockMvc.perform(get("/teams"))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedProjectsResponses)));
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the getFilteredTeams endpoint")
    void getFilteredTeams_NoExceptionThrown_OkResponse() throws Exception {
        TeamEntity firstExpectedTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("Team")
            .build();

        TeamEntity secondExpectedTeam = TeamEntity.builder()
            .teamId("TEAM2")
            .name("Team")
            .build();

        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "Team");

        Map<String, String> operators = new HashMap<>();
        operators.put("name", "=");

        Map<String, String> dataTypes = new HashMap<>();
        dataTypes.put("name", "string");

        FilterCriteria filterCriteria = new FilterCriteria(
            filters,
            operators,
            dataTypes
        );

        TeamFullResponse firstExpectedTeamResponse = mapStructMapper.toResponse(firstExpectedTeam);
        TeamFullResponse secondExpectedTeamResponse = mapStructMapper.toResponse(secondExpectedTeam);

        List<TeamEntity> expectedTeams = List.of(
            firstExpectedTeam,
            secondExpectedTeam
        );

        when(teamService.filterTeams(argThat(new FilterCriteriaMatcher(filterCriteria)))).thenReturn(expectedTeams);

        List<TeamFullResponse> expectedProjectsResponses = List.of(
            firstExpectedTeamResponse,
            secondExpectedTeamResponse
        );

        mockMvc.perform(post("/teams/filter")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(filterCriteria)))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedProjectsResponses)));
    }

    @Test
    @DisplayName("Should return OK Response when there are teams to return when calling the getFilteredTeams endpoint")
    void getFilteredTeams_ExistingTeams_OkResponse() throws Exception {
        TeamEntity firstExpectedTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("Team")
            .build();

        TeamEntity secondExpectedTeam = TeamEntity.builder()
            .teamId("TEAM2")
            .name("Team")
            .build();

        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "Team");

        Map<String, String> operators = new HashMap<>();
        operators.put("name", "=");

        Map<String, String> dataTypes = new HashMap<>();
        dataTypes.put("name", "string");

        FilterCriteria filterCriteria = new FilterCriteria(
            filters,
            operators,
            dataTypes
        );

        TeamFullResponse firstExpectedTeamResponse = mapStructMapper.toResponse(firstExpectedTeam);
        TeamFullResponse secondExpectedTeamResponse = mapStructMapper.toResponse(secondExpectedTeam);

        List<TeamEntity> expectedTeams = List.of(
            firstExpectedTeam,
            secondExpectedTeam
        );

        when(teamService.filterTeams(argThat(new FilterCriteriaMatcher(filterCriteria)))).thenReturn(expectedTeams);

        List<TeamFullResponse> expectedProjectsResponses = List.of(
            firstExpectedTeamResponse,
            secondExpectedTeamResponse
        );

        mockMvc.perform(post("/teams/filter")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(filterCriteria)))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedProjectsResponses)));
    }

    @Test
    @DisplayName("Should return OK Response when there are no teams to return when calling the getFilteredTeams endpoint")
    void getFilteredTeams_NoTeams_OkResponse() throws Exception {
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "Team");

        Map<String, String> operators = new HashMap<>();
        operators.put("name", "=");

        Map<String, String> dataTypes = new HashMap<>();
        dataTypes.put("name", "string");

        FilterCriteria filterCriteria = new FilterCriteria(
            filters,
            operators,
            dataTypes
        );
        
        List<TeamEntity> expectedTeams = List.of();

        when(teamService.filterTeams(argThat(new FilterCriteriaMatcher(filterCriteria)))).thenReturn(expectedTeams);

        List<TeamFullResponse> expectedProjectsResponses = List.of();

        mockMvc.perform(post("/teams/filter")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(filterCriteria)))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedProjectsResponses)));
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the getTeam endpoint")
    void getTeam_NoExceptionThrown_OkResponse() throws Exception {
        TeamEntity expectedTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .build();

        TeamFullResponse expectedTeamResponse = mapStructMapper.toResponse(expectedTeam);

        when(teamService.getTeamByTeamId("TEAM1")).thenReturn(expectedTeam);

        mockMvc.perform(get("/teams/{teamId}", "TEAM1"))
            .andExpect(status().isOk())
            .andExpect(content()
                .json(objectMapper.writeValueAsString(expectedTeamResponse)));
    }

    @Test
    @DisplayName("Should return NOT FOUND Response and resolve exception when TeamIdNotFoundException was thrown when calling the getTeam endpoint")
    void getTeam_TeamIdNotFoundException_ResolvedExceptionAndNotFoundResponse() throws Exception {
        String expectedTeamId = "TEAM1";

        doThrow(new TeamIdNotFoundException(expectedTeamId)).when(teamService).getTeamByTeamId(expectedTeamId);

        mockMvc.perform(get("/teams/{teamId}", expectedTeamId))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(TeamIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(TEAM_WITH_ID_NOT_FOUND, expectedTeamId));
    }

    @Test
    @DisplayName("Should return CREATED Response when no exception was thrown when calling the createTeam endpoint")
    void createTeam_NoExceptionThrown_CreatedResponse() throws Exception {
        TeamEntity expectedTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .build();

        TeamRequest expectedTeamRequest = mapStructMapper.toRequest(expectedTeam);

        when(teamService.getTeamByTeamId("TEAM1")).thenReturn(expectedTeam);

        mockMvc.perform(post("/teams")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(expectedTeamRequest)))
            .andExpect(status().isCreated());

        verify(teamService).saveTeam(teamCaptor.capture());

        TeamEntity actualTeam = teamCaptor.getValue();

        assertThat(actualTeam.getTeamId()).isEqualTo(expectedTeam.getTeamId());
        assertThat(actualTeam.getName()).isEqualTo(expectedTeam.getName());
    }

    @Test
    @DisplayName("Should return BAD REQUEST Response and resolve exception when  when calling the createTeam endpoint")
    void createTeam_TeamAlreadyCreatedExceptionThrown_CreatedResponse() throws Exception {
        String givenTeamId = "TEAM1";
        TeamEntity expectedTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .build();

        TeamRequest expectedTeamRequest = mapStructMapper.toRequest(expectedTeam);

        doThrow(new TeamAlreadyCreatedException(givenTeamId)).when(teamService).saveTeam(any());

        mockMvc.perform(post("/teams")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expectedTeamRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(TeamAlreadyCreatedException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(TEAM_ALREADY_CREATED, givenTeamId)));

        verify(teamService).saveTeam(teamCaptor.capture());

        TeamEntity actualTeam = teamCaptor.getValue();

        assertThat(actualTeam.getTeamId()).isEqualTo(expectedTeam.getTeamId());
        assertThat(actualTeam.getName()).isEqualTo(expectedTeam.getName());
    }

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the addColleagueInTeam endpoint")
    void addColleagueInTeam_NoExceptionThrown_OkResponse() throws Exception {
        String givenTeamId = "TEAM1";
        String givenUserId = "FU00001";

        mockMvc.perform(put("/teams/{teamId}/addColleague/{userId}", givenTeamId, givenUserId)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(teamService).addColleague(givenTeamId, givenUserId);
    }

    @Test
    @DisplayName("Should return NOT_FOUND Response when TeamIdNotFoundException was thrown when calling the addColleagueInTeam endpoint")
    void addColleagueInTeam_TeamIdNotFoundExceptionThrown_ResolvedExceptionAndNotFoundResponse() throws Exception {
        String givenTeamId = "TEAM1";
        String givenUserId = "FU00001";

        doThrow(new TeamIdNotFoundException(givenTeamId)).when(teamService).addColleague(givenTeamId, givenUserId);

        mockMvc.perform(put("/teams/{teamId}/addColleague/{userId}", givenTeamId, givenUserId)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(TeamIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(TEAM_WITH_ID_NOT_FOUND, givenTeamId)));

        verify(teamService).addColleague(givenTeamId, givenUserId);
    }

    @Test
    @DisplayName("Should return NOT_FOUND Response when UserIdNotFoundException was thrown when calling the addColleagueInTeam endpoint")
    void addColleagueInTeam_UserIdNotFoundExceptionThrown_ResolvedExceptionAndNotFoundResponse() throws Exception {
        String givenTeamId = "TEAM1";
        String givenUserId = "FU00001";

        doThrow(new UserIdNotFoundException(givenUserId)).when(teamService).addColleague(givenTeamId, givenUserId);

        mockMvc.perform(put("/teams/{teamId}/addColleague/{userId}", givenTeamId, givenUserId)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(USER_WITH_ID_NOT_FOUND, givenUserId)));

        verify(teamService).addColleague(givenTeamId, givenUserId);
    }
}
