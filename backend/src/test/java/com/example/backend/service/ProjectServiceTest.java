package com.example.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.time.ZoneId;

import com.example.backend.dao.ProjectDao;
import com.example.backend.dao.TeamDao;
import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.TeamEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.project.ProjectAlreadyCreatedException;
import com.example.backend.exception.project.ProjectNotFoundException;
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
class ProjectServiceTest {

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
    void setUp() {
        projectService = new ProjectService(
            projectDao,
            teamDao
        );
    }

    @Test
    @DisplayName("Should return a not empty list when there are projects")
    void getAllProjects_ExistingProjects() {
        ProjectEntity firstExpectedProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .build();

        ProjectEntity secondExpectedProject = ProjectEntity.builder()
            .projectKey("PROJECT2")
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
    void getAllProjects_NoProjects() {
        when(projectDao.selectAllProjects()).thenReturn(List.of());

        assertThat(projectService.getAllProjects()).isEmpty();
    }

    @Test
    @DisplayName("Should return a not empty list when there are projects to filter")
    void filterProjects_ExistingProjects() {
        ProjectEntity firstExpectedProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .build();

        ProjectEntity secondExpectedProject = ProjectEntity.builder()
            .projectKey("PROJECT2")
            .name("Second Project")
            .description("Second Project Description")
            .build();

        Map<String, Object> filters = new HashMap<>();
        filters.put("version", "v1.0");

        Map<String, String> operators = new HashMap<>();
        operators.put("version", "=");

        Map<String, String> dataTypes = new HashMap<>();
        dataTypes.put("version", "string");

        FilterCriteria filterCriteria = new FilterCriteria(
            filters,
            operators,
            dataTypes
        );

        List<ProjectEntity> expectedProjects = List.of(
            firstExpectedProject,
            secondExpectedProject
        );

        when(projectDao.selectAllFilteredProjects(filterCriteria))
            .thenReturn(List.of(
                firstExpectedProject,
                secondExpectedProject
            ));

        assertThat(projectService.filterProjects(filterCriteria)).isNotEmpty();
        assertThat(projectService.filterProjects(filterCriteria)).isEqualTo(expectedProjects);
    }

    @Test
    @DisplayName("Should return an empty list when there are no projects")
    void filterProjects_NoProjects() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("version", "v1.0");

        Map<String, String> operators = new HashMap<>();
        operators.put("version", "=");

        Map<String, String> dataTypes = new HashMap<>();
        dataTypes.put("version", "string");

        FilterCriteria filterCriteria = new FilterCriteria(
            filters,
            operators,
            dataTypes
        );

        when(projectDao.selectAllFilteredProjects(filterCriteria)).thenReturn(List.of());

        assertThat(projectService.filterProjects(filterCriteria)).isEmpty();
    }

    @Test
    @DisplayName("Should return a project by projectKey if exists")
    void getProjectByProjectKey_NoExceptionThrown() {
        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .build();

        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.of(expectedProject));

        assertThat(projectService.getProjectByProjectKey("FPC")).isEqualTo(expectedProject);
    }

    @Test
    @DisplayName("Should throw an exception when try to return a project by projectKey that doesn't exists")
    void getProjectByProjectKey_ProjectNotFoundExceptionThrown() {
        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.getProjectByProjectKey("FPC");
        }).isInstanceOf(ProjectNotFoundException.class)
        .hasMessage(String.format(PROJECT_WITH_ID_NOT_FOUND, "FPC"));
    }

    @Test
    @DisplayName("Should save project when it doesn't exist")
    void saveProject_NoExceptionThrown() {
        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .build();

        when(projectDao.existsProjectWithProjectKey("FPC")).thenReturn(false);

        projectService.saveProject(expectedProject);

        verify(projectDao, times(1)).insertProject(projectArgumentCaptor.capture());

        ProjectEntity capturedProject = projectArgumentCaptor.getValue();

        assertThat(capturedProject.getProjectKey()).isEqualTo(expectedProject.getProjectKey());
        assertThat(capturedProject.getName()).isEqualTo(expectedProject.getName());
        assertThat(capturedProject.getDescription()).isEqualTo(expectedProject.getDescription());
    }

    @Test
    @DisplayName("Should throw an exception when try to save a project that already exists")
    void saveProject_ProjectAlreadyCreatedExceptionThrown() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .build();

        when(projectDao.existsProjectWithProjectKey("FPC")).thenReturn(true);

        assertThatThrownBy(() -> {
            projectService.saveProject(existingProject);
        }).isInstanceOf(ProjectAlreadyCreatedException.class)
        .hasMessage(String.format(PROJECT_ALREADY_CREATED, "FPC"));
    }

    @Test
    @DisplayName("Should add existing team to existing project")
    void addTeam_NoExceptionThrown() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .teams(new HashSet<TeamEntity>())
            .build();

        TeamEntity existingTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .projects(new HashSet<ProjectEntity>())
            .build();

        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.of(existingProject));
        when(teamDao.selectTeamByTeamId("TEAM1")).thenReturn(Optional.of(existingTeam));

        projectService.addTeam("FPC", "TEAM1");

        verify(projectDao, times(1)).insertProject(projectArgumentCaptor.capture());

        ProjectEntity capturedProject = projectArgumentCaptor.getValue();

        assertThat(capturedProject.getProjectKey()).isEqualTo(existingProject.getProjectKey());
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
    void addTeam_TeamIdNotFoundExceptionThrown() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .teams(new HashSet<TeamEntity>())
            .build();

        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.of(existingProject));
        when(teamDao.selectTeamByTeamId("TEAM1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.addTeam("FPC", "TEAM1");
        }).isInstanceOf(TeamIdNotFoundException.class)
        .hasMessage(String.format(TEAM_WITH_ID_NOT_FOUND, "TEAM1"));
    }

    @Test
    @DisplayName("Should throw exception when add existing team to non-existent project")
    void addTeam_ProjectNotFoundExceptionThrown() {
        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.addTeam("FPC", "TEAM1");
        }).isInstanceOf(ProjectNotFoundException.class)
        .hasMessage(String.format(PROJECT_WITH_ID_NOT_FOUND, "FPC"));
    }

    @Test
    @DisplayName("Should return a not empty list when there are issues created on given project")
    void getAllIssuesOnProjectById_ExistingIssuesOnProjects() {
        IssueEntity firstExpectedIssue = IssueEntity.builder()
            .issueId("FPC-0001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        IssueEntity secondExpectedIssue = IssueEntity.builder()
            .issueId("FPC-0002")
            .title("Second Issue Title")
            .description("Second Issue Description")
            .build();

        List<IssueEntity> issuesOnProject = List.of(
            firstExpectedIssue,
            secondExpectedIssue
        );

        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .issues(issuesOnProject)
            .build();


        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.of(existingProject));

        List<IssueEntity> actualIssues = projectService.getAllIssuesOnProjectById("FPC");

        assertThat(actualIssues).isEqualTo(issuesOnProject);
    }

    @Test
    @DisplayName("Should return a not empty list when there are issues created on given project")
    void getAllIssuesOnProjectById_NoIssuesOnProject() {
        List<IssueEntity> issuesOnProject = List.of();

        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .issues(issuesOnProject)
            .build();


        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.of(existingProject));

        List<IssueEntity> actualIssues = projectService.getAllIssuesOnProjectById("FPC");

        assertThat(actualIssues).isEqualTo(issuesOnProject);
    }

    @Test
    @DisplayName("Should return a not empty list when there are issues created on given project")
    void getAllIssuesOnProjectById_ProjectNotFoundExceptionThrown() {
        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.getAllIssuesOnProjectById("FPC");
        }).isInstanceOf(ProjectNotFoundException.class)
        .hasMessage(String.format(PROJECT_WITH_ID_NOT_FOUND, "FPC"));
    }

    @Disabled
    @Test
    @DisplayName("Should return a not empty list when there are teams assigned on given project")
    void getAllTeamsOnProjectById_ExistingTeamsOnProjects() {
        TeamEntity firstExpectedTeam = TeamEntity.builder()
            .teamId("TEAM1")
            .name("First Team")
            .build();

        TeamEntity secondExpectedTeam = TeamEntity.builder()
            .teamId("TEAM2")
            .name("Second Team")
            .build();

        Set<TeamEntity> teamsOnProjectSet = Set.of(
            firstExpectedTeam,
            secondExpectedTeam
        );

        List<TeamEntity> teamsOnProjectList = List.of(
            firstExpectedTeam,
            secondExpectedTeam
        );

        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .teams(teamsOnProjectSet)
            .build();


        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.of(existingProject));

        List<TeamEntity> actualTeams = projectService.getAllTeamsOnProjectById("FPC");

        assertThat(actualTeams).isEqualTo(teamsOnProjectList);
    }

    @Test
    @DisplayName("Should return an empty list when there are no teams assigned on given project")
    void getAllIssuesOnProjectById_NoTeamsOnProject() {
        Set<TeamEntity> teamsOnProjectSet = Set.of();
        List<TeamEntity> teamsOnProjectList = List.of();


        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .teams(teamsOnProjectSet)
            .build();


        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.of(existingProject));

        List<TeamEntity> actualIssues = projectService.getAllTeamsOnProjectById("FPC");

        assertThat(actualIssues).isEqualTo(teamsOnProjectList);
    }

    @Test
    @DisplayName("Should throw an exception when try to return all teams on a project that doesn't exists")
    void getAllTeamsOnProjectById_ProjectNotFoundExceptionThrown() {
        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.getAllTeamsOnProjectById("FPC");
        }).isInstanceOf(ProjectNotFoundException.class)
        .hasMessage(String.format(PROJECT_WITH_ID_NOT_FOUND, "FPC"));
    }
}
