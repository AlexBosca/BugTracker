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

import com.example.backend.dao.ProjectDao;
import com.example.backend.dao.TeamDao;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.TeamEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.project.ProjectAlreadyCreatedException;
import com.example.backend.exception.project.ProjectIdNotFoundException;
import com.example.backend.exception.team.TeamIdNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static java.time.ZonedDateTime.of;

import static com.example.backend.util.ExceptionUtilities.PROJECT_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.PROJECT_ALREADY_CREATED;
import static com.example.backend.util.ExceptionUtilities.TEAM_WITH_ID_NOT_FOUND;

@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectDao projectDao;

    @Mock
    private TeamDao teamDao;

    private static ZonedDateTime NOW = of(2022,
                                            12,
                                            26, 
                                            11, 
                                            30, 
                                            0, 
                                            0, 
                                            ZoneId.of("GMT"));

    @Captor
    private ArgumentCaptor<ProjectEntity> projectArgumentCaptor;

    private ProjectService projectService;

    @BeforeEach
    void  setUp() {
        projectService = new ProjectService(
            projectDao,
            teamDao
        );
    }

    @Test
    @DisplayName("Should return a not empty list when there are projects")
    public void shouldGetAllProjectsWhenThereAreProjects() {
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

        List<ProjectEntity> expectedProjects = List.of(
            firstExpectedProject,
            secondExpectedProject
        );

        when(projectDao.selectAllProjects())
            .thenReturn(List.of(
                firstExpectedProject,
                secondExpectedProject
            ));

        assertThat(projectService.getAllProjects()).isNotEmpty();
        assertThat(projectService.getAllProjects()).isEqualTo(expectedProjects);
    }

    @Test
    @DisplayName("Should return an empty list when there are no projects")
    public void shouldGetEmptyWhenThereAreNoProjects() {
        when(projectDao.selectAllProjects()).thenReturn(List.of());

        assertThat(projectService.getAllProjects()).isEmpty();
    }

    @Test
    @DisplayName("Should return a project by projectId if exists")
    public void shouldFindProjectByProjectId() {
        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectId("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .build();

        when(projectDao.selectProjectById("PROJECT1")).thenReturn(Optional.of(expectedProject));

        assertThat(projectService.getProjectByProjectId("PROJECT1")).isEqualTo(expectedProject);
    }

    @Test
    @DisplayName("Should throw an exception when try to return a project by projectId that doesn't exists")
    public void shouldThrowExceptionWhenProjectToReturnByProjectIdDoesNotExist() {
        when(projectDao.selectProjectById("PROJECT1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.getProjectByProjectId("PROJECT1");
        }).isInstanceOf(ProjectIdNotFoundException.class)
        .hasMessage(String.format(PROJECT_WITH_ID_NOT_FOUND, "PROJECT1"));
    }

    @Test
    @DisplayName("Should save project when it doesn't exist")
    public void shouldSaveProjectWhenProjectDoesNotExist() {
        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectId("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .build();

        when(projectDao.existsProjectWithProjectId("PROJECT1")).thenReturn(false);

        projectService.saveProject(expectedProject);

        verify(projectDao, times(1)).insertProject(projectArgumentCaptor.capture());

        ProjectEntity capturedProject = projectArgumentCaptor.getValue();

        assertThat(capturedProject.getProjectId()).isEqualTo(expectedProject.getProjectId());
        assertThat(capturedProject.getName()).isEqualTo(expectedProject.getName());
        assertThat(capturedProject.getDescription()).isEqualTo(expectedProject.getDescription());
    }

    @Test
    @DisplayName("Should throw an exception when try to save a project that already exists")
    public void shouldThrowExceptionWhenProjectToSaveAlreadyExists() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectId("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .build();

        when(projectDao.existsProjectWithProjectId("PROJECT1")).thenReturn(true);

        assertThatThrownBy(() -> {
            projectService.saveProject(existingProject);
        }).isInstanceOf(ProjectAlreadyCreatedException.class)
        .hasMessage(String.format(PROJECT_ALREADY_CREATED, "PROJECT1"));
    }

    @Test
    @DisplayName("Should add existing team to existing project")
    public void shouldAddExistingTeamToExistingProject() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectId("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .teams(new HashSet<TeamEntity>())
            .build();

        TeamEntity existingTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .build();

        when(projectDao.selectProjectById("PROJECT1")).thenReturn(Optional.of(existingProject));
        when(teamDao.selectTeamByTeamId("TEAM1")).thenReturn(Optional.of(existingTeam));

        projectService.addTeam("PROJECT1", "TEAM1");

        verify(projectDao, times(1)).insertProject(projectArgumentCaptor.capture());

        ProjectEntity capturedProject = projectArgumentCaptor.getValue();

        assertThat(capturedProject.getProjectId()).isEqualTo(existingProject.getProjectId());
        assertThat(capturedProject.getName()).isEqualTo(existingProject.getName());
        assertThat(capturedProject.getDescription()).isEqualTo(existingProject.getDescription());
        assertThat(
            capturedProject.getTeams().stream()
                .filter(team -> "TEAM1".equals(team.getTeamId()))
                .findAny()
        ).isEqualTo(Optional.of(existingTeam));
    }

    @Test
    @DisplayName("Should throw exception when add non-existent team to existing project")
    public void shouldThrowExceptionWhenTeamToAddToProjectDoesNotExist() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectId("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .teams(new HashSet<TeamEntity>())
            .build();

        when(projectDao.selectProjectById("PROJECT1")).thenReturn(Optional.of(existingProject));
        when(teamDao.selectTeamByTeamId("TEAM1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.addTeam("PROJECT1", "TEAM1");
        }).isInstanceOf(TeamIdNotFoundException.class)
        .hasMessage(String.format(TEAM_WITH_ID_NOT_FOUND, "TEAM1"));
    }

    @Test
    @DisplayName("Should throw exception when add existing team to non-existent project")
    public void shouldThrowExceptionWhenAddTeamToProjectThatDoesNotExist() {
        when(projectDao.selectProjectById("PROJECT1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.addTeam("PROJECT1", "TEAM1");
        }).isInstanceOf(ProjectIdNotFoundException.class)
        .hasMessage(String.format(PROJECT_WITH_ID_NOT_FOUND, "PROJECT1"));
    }

    @Test
    @DisplayName("Should return a not empty list when there are issues created on given project")
    public void shouldGetAllIssuesOnProjectWhenThereAreIssuesOnGivenProject() {
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

        List<IssueEntity> issuesOnProject = List.of(
            firstExpectedIssue,
            secondExpectedIssue
        );

        ProjectEntity existingProject = ProjectEntity.builder()
            .projectId("PROJECT1")
            .name("First Project")
            .description("First Project Description")
            .issues(issuesOnProject)
            .build();


        when(projectDao.selectProjectById("PROJECT1")).thenReturn(Optional.of(existingProject));

        List<IssueEntity> actualIssues = projectService.getAllIssuesOnProjectById("PROJECT1");

        assertThat(actualIssues).isEqualTo(issuesOnProject);
    }
}
