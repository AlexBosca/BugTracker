package com.example.backend.service;

import static com.example.backend.util.ExceptionUtilities.PROJECT_ALREADY_CREATED;
import static com.example.backend.util.ExceptionUtilities.PROJECT_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.USER_ROLE_MISMATCH;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_ID_NOT_FOUND;
import static com.example.backend.util.project.ProjectLoggingMessages.PROJECT_UPDATED;
import static java.time.ZonedDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dao.ProjectDao;
import com.example.backend.dao.UserDao;
import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.ProjectUpdateRequest;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.enums.UserRole;
import com.example.backend.exception.project.ProjectAlreadyCreatedException;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;
import com.example.backend.exception.user.UserUnexpectedRoleException;
import com.example.backend.util.Utilities;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.classic.Logger;

@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectDao projectDao;

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
    private ArgumentCaptor<ProjectEntity> projectArgumentCaptor;

    private ProjectService projectService;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        projectService = new ProjectService(
            projectDao,
            userDao
        );

        Logger logger = (Logger) LoggerFactory.getLogger(ProjectService.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        
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
    @DisplayName("Should save project when it doesn't exist and project manager exists")
    void saveProject_NoExceptionThrown() {
        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .build();
        
        UserEntity existingProjectManager = UserEntity.builder()
            .userId("JC_12345")
            .firstName("Josh")
            .lastName("Carter")
            .email("josh.carter@gmail.com")
            .role(UserRole.ROLE_PROJECT_MANAGER)
            .build();

        when(projectDao.existsProjectWithProjectKey("FPC")).thenReturn(false);
        when(userDao.selectUserByUserId("JC_12345")).thenReturn(Optional.of(existingProjectManager));

        projectService.saveProject(expectedProject, "JC_12345");

        verify(projectDao, times(1)).insertProject(projectArgumentCaptor.capture());

        ProjectEntity capturedProject = projectArgumentCaptor.getValue();

        assertThat(capturedProject.getProjectKey()).isEqualTo(expectedProject.getProjectKey());
        assertThat(capturedProject.getName()).isEqualTo(expectedProject.getName());
        assertThat(capturedProject.getDescription()).isEqualTo(expectedProject.getDescription());
        assertThat(capturedProject.getProjectManager()).isEqualTo(existingProjectManager);
    }

    @Test
    @DisplayName("Should throw an exception when try to save a project on project manager that doesn't exist")
    void saveProject_UserIdNotFoundExceptionThrown() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .build();

        when(projectDao.existsProjectWithProjectKey("FPC")).thenReturn(false);
        when(userDao.selectUserByUserId("JC_12345")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.saveProject(existingProject, "JC_12345");
        }).isInstanceOf(UserIdNotFoundException.class)
        .hasMessage(String.format(USER_WITH_ID_NOT_FOUND, "JC_12345"));
    }

    @Test
    @DisplayName("Should throw an exception when try to save a project on user without ROLE_PROJECT_MANAGER")
    void saveProject_UserUnexpectedRoleExceptionThrown() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .build();

        UserEntity existingProjectManager = UserEntity.builder()
            .userId("JC_12345")
            .firstName("Josh")
            .lastName("Carter")
            .email("josh.carter@gmail.com")
            .role(UserRole.ROLE_DEVELOPER)
            .build();

        when(projectDao.existsProjectWithProjectKey("FPC")).thenReturn(false);
        when(userDao.selectUserByUserId("JC_12345")).thenReturn(Optional.of(existingProjectManager));

        assertThatThrownBy(() -> {
            projectService.saveProject(existingProject, "JC_12345");
        }).isInstanceOf(UserUnexpectedRoleException.class)
        .hasMessage(String.format(USER_ROLE_MISMATCH, "JC_12345", UserRole.ROLE_DEVELOPER.getName(), UserRole.ROLE_PROJECT_MANAGER.getName()));
    }

    @Test
    @DisplayName("Should throw an exception when try to save a project that already exists")
    void saveProject_ProjectAlreadyCreatedExceptionThrown() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .build();

        UserEntity existingProjectManager = UserEntity.builder()
            .userId("JC_12345")
            .firstName("Josh")
            .lastName("Carter")
            .email("josh.carter@gmail.com")
            .role(UserRole.ROLE_PROJECT_MANAGER)
            .build();

        when(projectDao.existsProjectWithProjectKey("FPC")).thenReturn(true);
        when(userDao.selectUserByUserId("JC_12345")).thenReturn(Optional.of(existingProjectManager));

        assertThatThrownBy(() -> {
            projectService.saveProject(existingProject, "JC_12345");
        }).isInstanceOf(ProjectAlreadyCreatedException.class)
        .hasMessage(String.format(PROJECT_ALREADY_CREATED, "FPC"));
    }

    @Test
    @DisplayName("Should update a project by projectKey that exists")
    void updateProject_NoExceptionThrown() {
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

        when(projectDao.existsProjectWithProjectKey(givenProjectKey)).thenReturn(true);

        projectService.updateProject(givenProjectKey, updateRequest);

        verify(projectDao, times(1)).updateProject(givenProjectKey, updateRequest);
        
        List<ILoggingEvent> logList = listAppender.list;
        assertThat(logList).hasSize(1);
        assertThat(logList.get(0).getFormattedMessage()).isEqualTo(Utilities.formattedString("Service - Project updated: %s", updateRequest.toString()));
    }

    @Test
    @DisplayName("Should throw an exception when try to update a project by projectKey that doesn't exist")
    void updateProject_ProjectNotFoundExceptionThrown() {
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

        when(projectDao.existsProjectWithProjectKey(givenProjectKey)).thenReturn(false);

        assertThatThrownBy(() -> {
            projectService.updateProject(givenProjectKey, updateRequest);
        }).isInstanceOf(ProjectNotFoundException.class)
        .hasMessage(String.format(PROJECT_WITH_ID_NOT_FOUND, "FPC"));

        verify(projectDao, never()).updateProject(anyString(), any());
        
        List<ILoggingEvent> logList = listAppender.list;
        assertThat(logList).isEmpty();
    }

    @Test
    @DisplayName("Should assign an user that exists to a project that exists")
    void assignUserOnProject_NoExceptionThrown() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .assignedUsers(Sets.newSet())
            .build();
        
        UserEntity existingUser = UserEntity.builder()
            .userId("JC_12345")
            .firstName("Josh")
            .lastName("Carter")
            .email("josh.carter@gmail.com")
            .role(UserRole.ROLE_DEVELOPER)
            .build();

        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.of(existingProject));
        when(userDao.selectUserByUserId("JC_12345")).thenReturn(Optional.of(existingUser));

        projectService.assignUserOnProject("FPC", "JC_12345");

        verify(projectDao, times(1)).insertProject(projectArgumentCaptor.capture());

        ProjectEntity capturedProject = projectArgumentCaptor.getValue();

        assertThat(capturedProject.getProjectKey()).isEqualTo(existingProject.getProjectKey());
        assertThat(capturedProject.getName()).isEqualTo(existingProject.getName());
        assertThat(capturedProject.getDescription()).isEqualTo(existingProject.getDescription());
        assertThat(capturedProject.getAssignedUsers()).isEqualTo(Set.of(existingUser));
    }

    @Test
    @DisplayName("Should throw an exception when try to assign an existing user to a non existing project")
    void assignUserOnProject_ProjectNotFoundExceptionThrown() {
        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.assignUserOnProject("FPC", "JC_12345");
        }).isInstanceOf(ProjectNotFoundException.class)
        .hasMessage(String.format(PROJECT_WITH_ID_NOT_FOUND, "FPC"));
    }

    @Test
    @DisplayName("Should throw an exception when try to assign a non existing user to an existing project")
    void assignUserOnProject_UserIdNotFoundExceptionThrown() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .assignedUsers(Sets.newSet())
            .build();

        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.of(existingProject));
        when(userDao.selectUserByUserId("JC_12345")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.assignUserOnProject("FPC", "JC_12345");
        }).isInstanceOf(UserIdNotFoundException.class)
        .hasMessage(String.format(USER_WITH_ID_NOT_FOUND, "JC_12345"));
    }

    @Test
    @DisplayName("Should assign users that exist to a project that exists")
    void assignUsersOnProject_NoExceptionThrown() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .assignedUsers(Sets.newSet())
            .build();
        
        UserEntity firstExistingUser = UserEntity.builder()
            .userId("JC_12345")
            .firstName("Josh")
            .lastName("Carter")
            .email("josh.carter@gmail.com")
            .role(UserRole.ROLE_DEVELOPER)
            .build();

        UserEntity secondExistingUser = UserEntity.builder()
            .userId("JD_12346")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@yahoo.com")
            .role(UserRole.ROLE_TESTER)
            .build();

        Set<UserEntity> usersToBeAssigned = Set.of(
            firstExistingUser,
            secondExistingUser
        );

        Set<String> usersIdsToBeAssigned = Set.of(
            firstExistingUser.getUserId(),
            secondExistingUser.getUserId()
        );

        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.of(existingProject));
        when(userDao.selectUserByUserId("JC_12345")).thenReturn(Optional.of(firstExistingUser));
        when(userDao.selectUserByUserId("JD_12346")).thenReturn(Optional.of(secondExistingUser));

        projectService.assignUsersOnProject("FPC", usersIdsToBeAssigned);

        verify(projectDao, times(1)).insertProject(projectArgumentCaptor.capture());

        ProjectEntity capturedProject = projectArgumentCaptor.getValue();

        assertThat(capturedProject.getProjectKey()).isEqualTo(existingProject.getProjectKey());
        assertThat(capturedProject.getName()).isEqualTo(existingProject.getName());
        assertThat(capturedProject.getDescription()).isEqualTo(existingProject.getDescription());
        assertThat(capturedProject.getAssignedUsers()).isEqualTo(usersToBeAssigned);
    }

    @Test
    @DisplayName("Should throw an exception when try to assign existing users to a non existing project")
    void assignUsersOnProject_ProjectNotFoundExceptionThrown() {
        UserEntity firstExistingUser = UserEntity.builder()
            .userId("JC_12345")
            .firstName("Josh")
            .lastName("Carter")
            .email("josh.carter@gmail.com")
            .role(UserRole.ROLE_DEVELOPER)
            .build();

        UserEntity secondExistingUser = UserEntity.builder()
            .userId("JD_12346")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@yahoo.com")
            .role(UserRole.ROLE_TESTER)
            .build();
        
        Set<String> usersIdsToBeAssigned = Set.of(
            firstExistingUser.getUserId(),
            secondExistingUser.getUserId()
        );

        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.assignUsersOnProject("FPC", usersIdsToBeAssigned);
        }).isInstanceOf(ProjectNotFoundException.class)
        .hasMessage(String.format(PROJECT_WITH_ID_NOT_FOUND, "FPC"));
    }

    @Test
    @DisplayName("Should throw an exception when try to assign non existing users to an existing project")
    void assignUsersOnProject_UserIdNotFoundExceptionThrown() {
        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FPC")
            .name("First Project")
            .description("First Project Description")
            .assignedUsers(Sets.newSet())
            .build();
        
        Set<String> usersIdsToBeAssigned = Set.of("JC_12345", "JD_12346");

        when(projectDao.selectProjectByKey("FPC")).thenReturn(Optional.of(existingProject));
        when(userDao.selectUserByUserId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            projectService.assignUsersOnProject("FPC", usersIdsToBeAssigned);
        }).isInstanceOf(UserIdNotFoundException.class)
        .hasMessage(String.format(USER_WITH_ID_NOT_FOUND, "JC_12345"));
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
}
