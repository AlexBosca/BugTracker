package com.example.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.time.ZoneId;

import com.example.backend.dao.TeamDao;
import com.example.backend.dao.UserDao;
import com.example.backend.entity.TeamEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.team.TeamAlreadyCreatedException;
import com.example.backend.exception.team.TeamIdNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static java.time.ZonedDateTime.of;

import static com.example.backend.util.ExceptionUtilities.TEAM_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.TEAM_ALREADY_CREATED;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_ID_NOT_FOUND;

@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamDao teamDao;

    @Mock
    private UserDao userDao;

    private static ZonedDateTime NOW = of(2022,
                                            12,
                                            26, 
                                            11, 
                                            30, 
                                            0, 
                                            0, 
                                            ZoneId.of("GMT"));

    @Captor
    private ArgumentCaptor<TeamEntity> teamArgumentCaptor;

    private TeamService teamService;

    @BeforeEach
    void setUp() {
        teamService = new TeamService(
            teamDao,
            userDao
        );
    }

    @Test
    @DisplayName("Should return a not empty list when there are teams")
    void shouldGetAllTeamsWhenThereAreProjects() {
        TeamEntity firstExpectedTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .build();

        TeamEntity secondExpectedTeam = TeamEntity.builder()
            .teamId("TEAM2")
            .name("Second Team")
            .build();

        List<TeamEntity> expectedTeams = List.of(
            firstExpectedTeam,
            secondExpectedTeam
        );

        when(teamDao.selectAllTeams())
            .thenReturn(List.of(
                firstExpectedTeam,
                secondExpectedTeam
            ));

        assertThat(teamService.getAllTeams()).isNotEmpty();
        assertThat(teamService.getAllTeams()).isEqualTo(expectedTeams);
    }

    @Test
    @DisplayName("Should return an empty list when there are no teams")
    void souldGetEmptyListWhenThereAreNoTeams() {
        when(teamDao.selectAllTeams()).thenReturn(List.of());

        assertThat(teamService.getAllTeams()).isEmpty();
    }

    @Test
    @DisplayName("Should return a team by teamId when it exist")
    void shouldFindTeamByTeamId() {
        TeamEntity expectedTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .build();

        when(teamDao.selectTeamByTeamId("TEAM1")).thenReturn(Optional.of(expectedTeam));

        assertThat(teamService.getTeamByTeamId("TEAM1")).isEqualTo(expectedTeam);
    }

    @Test
    @DisplayName("Should throw an exception then try to return a team by teamId that doesn't exists")
    void shouldThrowExceptionWhenTeamToReturnByTeamIdDoesNotExists() {
        when(teamDao.selectTeamByTeamId("TEAM1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            teamService.getTeamByTeamId("TEAM1");
        }).isInstanceOf(TeamIdNotFoundException.class)
        .hasMessage(String.format(TEAM_WITH_ID_NOT_FOUND, "TEAM1"));
    }

    @Test
    @DisplayName("Should save team when it doesn't exist")
    void shouldSaveTeamWhenTeamDoesNotExists() {
        TeamEntity expectedTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .build();

        when(teamDao.existsTeamWithTeamId("TEAM1")).thenReturn(false);

        teamService.saveTeam(expectedTeam);

        verify(teamDao, times(1)).insertTeam(teamArgumentCaptor.capture());

        TeamEntity capturedTeam = teamArgumentCaptor.getValue();

        assertThat(capturedTeam.getTeamId()).isEqualTo(expectedTeam.getTeamId());
        assertThat(capturedTeam.getTeamId()).isEqualTo(expectedTeam.getTeamId());
    }

    @Test
    @DisplayName("Should throw an exception when try to save a team that already exists")
    void shouldThrowExceptionWhenTeamToSaveAlreadyExists() {
        TeamEntity existingTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .build();

        when(teamDao.existsTeamWithTeamId("TEAM1")).thenReturn(true);

        assertThatThrownBy(() -> {
            teamService.saveTeam(existingTeam);
        }).isInstanceOf(TeamAlreadyCreatedException.class)
        .hasMessage(String.format(TEAM_ALREADY_CREATED, "TEAM1"));
    }

    @Test
    @DisplayName("Should add existing user to existing team")
    void shouldAddExistingUserToExistingTeam() {
        TeamEntity existingTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .colleagues(new HashSet<UserEntity>())
            .build();

        UserEntity existingUser = UserEntity.builder()
            .userId("JD_00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .build();

        when(teamDao.selectTeamByTeamId("TEAM1")).thenReturn(Optional.of(existingTeam));
        when(userDao.selectUserByUserId("JD_00001")).thenReturn(Optional.of(existingUser));

        teamService.addColleague("TEAM1", "JD_00001");

        verify(teamDao, times(1)).insertTeam(teamArgumentCaptor.capture());

        TeamEntity capturedTeam = teamArgumentCaptor.getValue();

        assertThat(capturedTeam.getTeamId()).isEqualTo(existingTeam.getTeamId());
        assertThat(capturedTeam.getName()).isEqualTo(existingTeam.getName());
        assertThat(
            capturedTeam.getColleagues().stream()
                .filter(user -> "JD_00001".equals(user.getUserId()))
                .findAny()
        ).isEqualTo(Optional.of(existingUser));
    }

    @Test
    @DisplayName("Should throw exception when add non-existent user to existing team")
    void shouldThrowExceptionWhenUserToAddToExistingTeamDowsNotExist() {
        TeamEntity existingTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .colleagues(new HashSet<UserEntity>())
            .build();

        when(teamDao.selectTeamByTeamId("TEAM1")).thenReturn(Optional.of(existingTeam));
        when(userDao.selectUserByUserId("JD_00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            teamService.addColleague("TEAM1", "JD_00001");
        }).isInstanceOf(UserIdNotFoundException.class)
        .hasMessage(String.format(USER_WITH_ID_NOT_FOUND, "JD_00001"));
    }

    @Test
    @DisplayName("Should throw exception when add existing user to non-existent team")
    void shouldThrowExceptionWhenAddUserToProjectThatDoesNotExixst() {
        when(teamDao.selectTeamByTeamId("TEAM1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            teamService.addColleague("TEAM1", "JD_00001");
        }).isInstanceOf(TeamIdNotFoundException.class)
        .hasMessage(String.format(TEAM_WITH_ID_NOT_FOUND, "TEAM1"));
    }
}
