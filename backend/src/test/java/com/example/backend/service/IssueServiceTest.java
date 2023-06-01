package com.example.backend.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

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

import com.example.backend.dao.IssueCommentDao;
import com.example.backend.dao.IssueDao;
import com.example.backend.dao.ProjectDao;
import com.example.backend.dao.UserDao;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.enums.IssueStatus;
import com.example.backend.exception.issue.IssueAlreadyCreatedException;
import com.example.backend.exception.issue.IssueNotFoundException;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.exception.user.UserEmailNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;

import static java.time.ZonedDateTime.of;
import static java.time.LocalDateTime.now;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static com.example.backend.util.ExceptionUtilities.ISSUE_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.ISSUE_ALREADY_CREATED;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_EMAIL_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.PROJECT_WITH_ID_NOT_FOUND;

@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueDao issueDao;
    
    @Mock
    private IssueCommentDao commentDao;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserDao userDao;

    @Mock
    private Clock clock;

    @Captor
    private ArgumentCaptor<IssueEntity> issueArgumentCaptor;

    private static ZonedDateTime NOW = of(2022,
                                          12,
                                          26, 
                                          11, 
                                          30, 
                                          0, 
                                          0, 
                                          ZoneId.of("GMT"));

    private IssueService issueService;

    @BeforeEach
    void setUp() {
        issueService = new IssueService(
            issueDao,
            commentDao,
            projectDao,
            userDao,
            clock
        );
    }

    @Test
    @DisplayName("Should return a not empty list when there are issues")
    void shouldGetAllIssuesWhenThereAreIssues() {
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

        List<IssueEntity> expectedIssues = List.of(
            firstExpectedIssue,
            secondExpectedIssue
        );

        when(issueDao.selectAllIssues())
            .thenReturn(List.of(
                firstExpectedIssue,
                secondExpectedIssue
            ));

        assertThat(issueService.getAllIssues()).isNotEmpty();
        assertThat(issueService.getAllIssues()).isEqualTo(expectedIssues);  
    }

    @Test
    @DisplayName("Should return an empty list when there are no issues")
    void shouldGetEmptyListWhenThereAreNoIssues() {
        when(issueDao.selectAllIssues()).thenReturn(List.of());

        assertThat(issueService.getAllIssues()).isEmpty();
    }
    
    @Test
    @DisplayName("Should return an issue by issueId when it exist")
    void shouldFindIssueByIssueId() {
        IssueEntity expectedIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        when(issueDao.selectIssueByIssueId("00001")).thenReturn(Optional.of(expectedIssue));

        assertThat(issueService.getIssueByIssueId("00001")).isEqualTo(expectedIssue);
    }

    @Test
    @DisplayName("Should throw an exception when try to return an issue by issueId that doesn't exist")
    void shouldThrowExceptionIfIssueToReturnByIssueIdDoesNotExists() {
        when(issueDao.selectIssueByIssueId("00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.getIssueByIssueId("00001");
        }).isInstanceOf(IssueNotFoundException.class)
        .hasMessage(String.format(ISSUE_WITH_ID_NOT_FOUND, "00001"));
    }

    @Test
    @DisplayName("Should save issue related to an existing project by a registered user")
    void shouldSaveIssueOnExistingProjectByRegisteredUser() {
        IssueEntity issueToSave = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FP_00001")
            .name("First Project")
            .description("First project description")
            .build();

        UserEntity registeredUser =  UserEntity.builder()
            .email("john.doe@gmail.com")
            .firstName("John")
            .lastName("Doe")
            .build();

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());
        
        when(projectDao.selectProjectByKey("FP_00001")).thenReturn(Optional.of(existingProject));
        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.of(registeredUser));

        issueService.saveIssue(issueToSave, "FP_00001", "john.doe@gmail.com");

        verify(issueDao, times(1)).insertIssue(issueArgumentCaptor.capture());

        IssueEntity capturedIssue = issueArgumentCaptor.getValue();

        assertThat(capturedIssue.getIssueId()).isEqualTo(issueToSave.getIssueId());
        assertThat(capturedIssue.getTitle()).isEqualTo(issueToSave.getTitle());
        assertThat(capturedIssue.getDescription()).isEqualTo(issueToSave.getDescription());
        assertThat(capturedIssue.getCreatedByUser().getEmail()).isEqualTo(registeredUser.getEmail());
        assertThat(capturedIssue.getCreatedByUser().getFirstName()).isEqualTo(registeredUser.getFirstName());
        assertThat(capturedIssue.getCreatedByUser().getLastName()).isEqualTo(registeredUser.getLastName());
        assertThat(capturedIssue.getProject().getProjectKey()).isEqualTo(existingProject.getProjectKey());
        assertThat(capturedIssue.getProject().getName()).isEqualTo(existingProject.getName());
        assertThat(capturedIssue.getProject().getDescription()).isEqualTo(existingProject.getDescription());
    }

    @Test
    @DisplayName("Should throw an exception when try to save an issue that already exists")
    void shouldThrowExceptionWhenIssueToSaveAlreadyExists() {
        IssueEntity existingIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FP_00001")
            .name("First Project")
            .description("First project description")
            .build();

        UserEntity registeredUser =  UserEntity.builder()
            .email("john.doe@gmail.com")
            .firstName("John")
            .lastName("Doe")
            .build();

        when(projectDao.selectProjectByKey("FP_00001")).thenReturn(Optional.of(existingProject));
        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.of(registeredUser));
        when(issueDao.existsIssueWithIssueId("00001")).thenReturn(true);

        assertThatThrownBy(() -> {
            issueService.saveIssue(existingIssue, "FP_00001", "john.doe@gmail.com");
        }).isInstanceOf(IssueAlreadyCreatedException.class)
        .hasMessage(String.format(ISSUE_ALREADY_CREATED, "00001"));
    }

    @Test
    @DisplayName("Should throw an exception when try to save an issue on a project that doesn't exist")
    void shouldThrowExceptionWhenProjectToSaveIssueOnDoesNotExists() {
        IssueEntity existingIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        when(projectDao.selectProjectByKey("FP_00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.saveIssue(existingIssue, "FP_00001", "john.doe@gmail.com");
        }).isInstanceOf(ProjectNotFoundException.class)
        .hasMessage(String.format(PROJECT_WITH_ID_NOT_FOUND, "FP_00001"));
    }

    @Test
    @DisplayName("Should throw an exception when try to save an issue by a user that doesn't exist")
    void shouldThrowExceptionWhenUserToSaveIssueByDoesNotExists() {
        IssueEntity existingIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        ProjectEntity existingProject = ProjectEntity.builder()
            .projectKey("FP_00001")
            .name("First Project")
            .description("First project description")
            .build();

            when(projectDao.selectProjectByKey("FP_00001")).thenReturn(Optional.of(existingProject));
            when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                issueService.saveIssue(existingIssue, "FP_00001", "john.doe@gmail.com");
            }).isInstanceOf(UserEmailNotFoundException.class)
            .hasMessage(USER_WITH_EMAIL_NOT_FOUND, "john.doe@gmail.com");
    }

    @Test
    @DisplayName("Should assign existing issue to registered user")
    void shouldAssignExistingIssueToRegisteredUser() {
        IssueEntity existingIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        UserEntity assigneeUser =  UserEntity.builder()
            .userId("JD_00001")
            .email("john.doe@gmail.com")
            .firstName("John")
            .lastName("Doe")
            .build();

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        when(issueDao.selectIssueByIssueId("00001")).thenReturn(Optional.of(existingIssue));
        when(userDao.selectUserByUserId("JD_00001")).thenReturn(Optional.of(assigneeUser));

        issueService.assignToUser("00001", "JD_00001");

        verify(issueDao, times(1)).updateIssue(issueArgumentCaptor.capture());

        IssueEntity capturedIssue = issueArgumentCaptor.getValue();

        assertThat(capturedIssue.getIssueId()).isEqualTo(existingIssue.getIssueId());
        assertThat(capturedIssue.getTitle()).isEqualTo(existingIssue.getTitle());
        assertThat(capturedIssue.getDescription()).isEqualTo(existingIssue.getDescription());
        assertThat(capturedIssue.getAssignedOn()).isEqualTo(now(clock));
        assertThat(capturedIssue.getAssignedUser().getUserId()).isEqualTo(assigneeUser.getUserId());
        assertThat(capturedIssue.getAssignedUser().getFirstName()).isEqualTo(assigneeUser.getFirstName());
        assertThat(capturedIssue.getAssignedUser().getLastName()).isEqualTo(assigneeUser.getLastName());
        assertThat(capturedIssue.getAssignedUser().getEmail()).isEqualTo(assigneeUser.getEmail());
    }

    @Test
    @DisplayName("Should throw exception when try to assign an issue that doesn't exist to a registered user")
    void shouldThrowExceptionWhenAssignIssueThatDoesNotExist() {
        when(issueDao.selectIssueByIssueId("00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.assignToUser("00001", "JD_00001");
        }).isInstanceOf(IssueNotFoundException.class)
        .hasMessage(ISSUE_WITH_ID_NOT_FOUND, "00001");
    }

    @Test
    @DisplayName("Should throw exception when try to assign an existing issue to a not registered user")
    void shouldThrowExceptionWhenAssignIssueNotRegisteredUser() {
        IssueEntity existingIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        when(issueDao.selectIssueByIssueId("00001")).thenReturn(Optional.of(existingIssue));
        when(userDao.selectUserByUserId("JD_00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.assignToUser("00001", "JD_00001");
        }).isInstanceOf(UserIdNotFoundException.class)
        .hasMessage(USER_WITH_ID_NOT_FOUND, "JD_00001");
    }

    @Test
    @DisplayName("Should close existing issue by registered user")
    void shouldCloseExistingIssueByRegisteredUser() {
        IssueEntity existingIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        UserEntity registeredUser =  UserEntity.builder()
            .userId("JD_00001")
            .email("john.doe@gmail.com")
            .firstName("John")
            .lastName("Doe")
            .build();

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        when(issueDao.selectIssueByIssueId("00001")).thenReturn(Optional.of(existingIssue));
        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.of(registeredUser));

        issueService.closeByUser("00001", "john.doe@gmail.com");

        verify(issueDao, times(1)).updateIssue(issueArgumentCaptor.capture());

        IssueEntity capturedIssue = issueArgumentCaptor.getValue();

        assertThat(capturedIssue.getIssueId()).isEqualTo(existingIssue.getIssueId());
        assertThat(capturedIssue.getTitle()).isEqualTo(existingIssue.getTitle());
        assertThat(capturedIssue.getDescription()).isEqualTo(existingIssue.getDescription());
        assertThat(capturedIssue.getClosedOn()).isEqualTo(now(clock));
        assertThat(capturedIssue.getClosedByUser().getUserId()).isEqualTo(registeredUser.getUserId());
        assertThat(capturedIssue.getClosedByUser().getFirstName()).isEqualTo(registeredUser.getFirstName());
        assertThat(capturedIssue.getClosedByUser().getLastName()).isEqualTo(registeredUser.getLastName());
        assertThat(capturedIssue.getClosedByUser().getEmail()).isEqualTo(registeredUser.getEmail());
    }

    @Test
    @DisplayName("Should throw exception when try to close an issue that doesn't exist by a registered user")
    void shouldThrowExceptionWhenCloseIssueThatDoesNotExist() {
        when(issueDao.selectIssueByIssueId("00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.closeByUser("00001", "john.doe@gmail.com");
        }).isInstanceOf(IssueNotFoundException.class)
        .hasMessage(ISSUE_WITH_ID_NOT_FOUND, "00001");
    }

    @Test
    @DisplayName("Should throw exception when try to close an existing issue by a not registered user")
    void shouldThrowExceptionWhenCloseIssueByNotRegisteredUser() {
        IssueEntity existingIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        when(issueDao.selectIssueByIssueId("00001")).thenReturn(Optional.of(existingIssue));
        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.closeByUser("00001", "john.doe@gmail.com");
        }).isInstanceOf(UserEmailNotFoundException.class)
        .hasMessage(USER_WITH_EMAIL_NOT_FOUND, "john.doe@gmail.com");
    }

    @Test
    @DisplayName("Should change state of existing issue")
    void shouldChangeStateOfExistingIssue() {
        IssueEntity existingIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .status(IssueStatus.NEW)
            .build();

        UserEntity registeredUser =  UserEntity.builder()
            .userId("JD_00001")
            .email("john.doe@gmail.com")
            .firstName("John")
            .lastName("Doe")
            .build();

        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        when(issueDao.selectIssueByIssueId("00001")).thenReturn(Optional.of(existingIssue));
        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.of(registeredUser));

        issueService.changeIssueStatus("00001", IssueStatus.ASSIGNED, "john.doe@gmail.com");

        verify(issueDao, times(1)).updateIssue(issueArgumentCaptor.capture());

        IssueEntity capturedIssue = issueArgumentCaptor.getValue();

        assertThat(capturedIssue.getIssueId()).isEqualTo(existingIssue.getIssueId());
        assertThat(capturedIssue.getTitle()).isEqualTo(existingIssue.getTitle());
        assertThat(capturedIssue.getDescription()).isEqualTo(existingIssue.getDescription());
        assertThat(capturedIssue.getStatus()).isEqualTo(IssueStatus.ASSIGNED);
        assertThat(capturedIssue.getModifiedOn()).isEqualTo(now(clock));
        assertThat(capturedIssue.getModifiedByUser().getUserId()).isEqualTo(registeredUser.getUserId());
        assertThat(capturedIssue.getModifiedByUser().getFirstName()).isEqualTo(registeredUser.getFirstName());
        assertThat(capturedIssue.getModifiedByUser().getLastName()).isEqualTo(registeredUser.getLastName());
        assertThat(capturedIssue.getModifiedByUser().getEmail()).isEqualTo(registeredUser.getEmail());
    }

    @Test
    @DisplayName("Should throw exception when try to change state of an issue that doesn't exist")
    void shouldThrowExceptionWhenChangeStateOfIssueThatDoesNotExist() {
        when(issueDao.selectIssueByIssueId("00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.changeIssueStatus("00001", IssueStatus.ASSIGNED, "john.doe@gmail.com");
        }).isInstanceOf(IssueNotFoundException.class)
        .hasMessage(ISSUE_WITH_ID_NOT_FOUND, "00001");
    }

    @Test
    @DisplayName("Should throw exception when try to change state of an existing issue by a not registered user")
    void shouldThrowExceptionWhenChangeStateOfIssueByNotRegisteredUser() {
        IssueEntity existingIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        when(issueDao.selectIssueByIssueId("00001")).thenReturn(Optional.of(existingIssue));
        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.changeIssueStatus("00001", IssueStatus.ASSIGNED, "john.doe@gmail.com");
        }).isInstanceOf(UserEmailNotFoundException.class)
        .hasMessage(USER_WITH_EMAIL_NOT_FOUND, "john.doe@gmail.com");
    }
}
