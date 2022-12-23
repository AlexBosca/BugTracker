package com.example.backend.service;

import static org.mockito.Mockito.when;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.dao.IssueCommentRepository;
import com.example.backend.dao.IssueRepository;
import com.example.backend.dao.ProjectRepository;
import com.example.backend.dao.UserRepository;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.issue.IssueIdNotFoundException;
import com.example.backend.exception.project.ProjectIdNotFoundException;
import com.example.backend.exception.user.UserEmailNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static com.example.backend.util.ExceptionUtilities.ISSUE_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_ID_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_EMAIL_NOT_FOUND;
import static com.example.backend.util.ExceptionUtilities.PROJECT_WITH_ID_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;
    
    @Mock
    private IssueCommentRepository commentRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Clock clock;

    private IssueService issueService;

    @BeforeEach
    void setUp() {
        issueService = new IssueService(issueRepository,
                                        commentRepository,
                                        projectRepository,
                                        userRepository,
                                        clock);
    }

    @Test
    @DisplayName("Should return a not empty list when there are issues")
    public void shouldGetAllIssuesWhenThereAreIssues() {
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

	when(issueRepository.findAll()).thenReturn(List.of(firstExpectedIssue, secondExpectedIssue));

	assertThat(issueService.getAllIssues()).isNotEmpty();
    }

    @Test
    @DisplayName("Should return an empty list when there are no issues")
    public void shouldGetAllIssuesWhenThereAreNoIssues() {
        when(issueRepository.findAll()).thenReturn(List.of());

        assertThat(issueService.getAllIssues()).isEmpty();
    }
    
    @Test
    @DisplayName("Should return an issue by issueId when it exists")
    public void shouldFindIssueByIssueId() {
        IssueEntity expectedIssue = IssueEntity.builder()
            .issueId("00001")
            .title("First Issue Title")
            .description("First Issue Description")
            .build();

        when(issueRepository.findByIssueId("00001")).thenReturn(Optional.of(expectedIssue));

        assertThat(issueService.findIssue("00001")).isEqualTo(expectedIssue);
    }

    @Test
    @DisplayName("Should throw an exception when try to return an isssue by issueId that doesn't exists")
    public void shouldThrowExceptionIfIssueWithToReturnByIssueIdDoesNotExists() {
        when(issueRepository.findByIssueId("00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.findIssue("00001");
        }).isInstanceOf(IssueIdNotFoundException.class)
        .hasMessage(String.format(ISSUE_WITH_ID_NOT_FOUND, "00001"));
    }

    @Test
    @DisplayName("Should return an user by userId when it exists")
    public void shouldFindUserByUserId() {
        UserEntity expectedUser =  UserEntity.builder()
            .userId("JD_00001")
            .firstName("John")
            .lastName("Doe")
            .build();

        when(userRepository.findByUserId("JD_00001")).thenReturn(Optional.of(expectedUser));

        assertThat(issueService.findUserByUserId("JD_00001")).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("Should throw an exception when try to return an user by userId that doesn't exists")
    public void shouldThrowExceptionIfUserToReturnByUserIdDoesNotExists() {
        when(userRepository.findByUserId("JD_00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.findUserByUserId("JD_00001");
        }).isInstanceOf(UserIdNotFoundException.class)
        .hasMessage(String.format(USER_WITH_ID_NOT_FOUND, "JD_00001"));
    }

    @Test
    @DisplayName("Should return an user by email when it exists")
    public void shouldFindUserByEmail() {
        UserEntity expectedUser =  UserEntity.builder()
            .email("john.doe@gmail.com")
            .firstName("John")
            .lastName("Doe")
            .build();

        when(userRepository.findByEmail("john.doe@gmail.com")).thenReturn(Optional.of(expectedUser));

        assertThat(issueService.findUserByEmail("john.doe@gmail.com")).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("Should throw an exception when try to return an user by email that doesn't exists")
    public void shouldThrowExceptionIfUserToReturnByEmailDoesNotExists() {
        when(userRepository.findByEmail("john.doe@gmail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.findUserByEmail("john.doe@gmail.com");
        }).isInstanceOf(UserEmailNotFoundException.class)
        .hasMessage(String.format(USER_WITH_EMAIL_NOT_FOUND, "john.doe@gmail.com"));
    }

    @Test
    @DisplayName("Should return a project by projectId when it exists")
    public void shouldFindProjectByProjectId() {
        ProjectEntity expectedProject = ProjectEntity.builder()
            .projectId("FP_00001")
            .name("First Project")
            .description("First project description")
            .build();

        when(projectRepository.findByProjectId("FP_00001")).thenReturn(Optional.of(expectedProject));

        assertThat(issueService.findProject("FP_00001")).isEqualTo(expectedProject);
    }

    @Test
    @DisplayName("Should throw an exception when try to return a project by projectId that doesn't exists")
    public void shouldThrowExceptionIfProjectToReturnByProjectIdDoesNotExists() {
        when(projectRepository.findByProjectId("FP_00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            issueService.findProject("FP_00001");
        }).isInstanceOf(ProjectIdNotFoundException.class)
        .hasMessage(String.format(PROJECT_WITH_ID_NOT_FOUND, "FP_00001"));
    }
}
