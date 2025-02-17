package ro.alexportfolio.backend.unittests.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ro.alexportfolio.backend.dao.IssueRepository;
import ro.alexportfolio.backend.dao.ProjectRepository;
import ro.alexportfolio.backend.exception.ExceptionMessages;
import ro.alexportfolio.backend.exception.IssueNotFoundException;
import ro.alexportfolio.backend.exception.IssueOrProjectNotFoundException;
import ro.alexportfolio.backend.exception.ProjectNotFoundException;
import ro.alexportfolio.backend.model.Issue;
import ro.alexportfolio.backend.service.EmailSenderService;
import ro.alexportfolio.backend.service.IssueService;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private Clock clock;

    @Mock
    private EmailSenderService emailSenderService;

    @Captor
    private ArgumentCaptor<Issue> issueCaptor;

    private IssueService issueService;

    private static final ZonedDateTime NOW = ZonedDateTime.of(2022,
                                                       12,
                                                       26,
                                                       11,
                                                       30,
                                                       0,
                                                       0,
                                                       ZoneId.of("GMT"));

    @BeforeEach
    void setUp() {
        issueService = new IssueService(issueRepository,
                                        projectRepository,
                                        clock,
                                        emailSenderService);
    }

    @Test
    void createIssue_ExistingProject() {
        // given
        String projectKey = "TEST";

        Issue issue = new Issue();
        issue.setIssueId("TEST-1");
        issue.setTitle("Test title");
        issue.setDescription("Test description");
        issue.setStatus("OPEN");

        // when
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(clock.getZone()).thenReturn(NOW.getZone());
        when(projectRepository.existsByProjectKey(projectKey)).thenReturn(true);

        issueService.createIssue(projectKey, issue);

        // then
        verify(issueRepository, times(1)).save(issueCaptor.capture());

        Issue capturedIssue = issueCaptor.getValue();

        assertThat(capturedIssue.getIssueId()).isEqualTo("TEST-1");
        assertThat(capturedIssue.getTitle()).isEqualTo("Test title");
        assertThat(capturedIssue.getDescription()).isEqualTo("Test description");
        assertThat(capturedIssue.getStatus()).isEqualTo("OPEN");
        assertThat(capturedIssue.getProjectKey()).isEqualTo(projectKey);
        assertThat(capturedIssue.getCreatedAt()).isEqualTo(NOW.toLocalDateTime());
    }

    @Test
    void createIssue_NonExistingProject() {
        // given
        String projectKey = "TEST";

        Issue issue = new Issue();
        issue.setIssueId("TEST-1");
        issue.setTitle("Test title");
        issue.setDescription("Test description");
        issue.setStatus("OPEN");

        // when
        when(projectRepository.existsByProjectKey(projectKey)).thenReturn(false);

        // then
        assertThatThrownBy(() -> issueService.createIssue(projectKey, issue))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage(ExceptionMessages.PROJECT_NOT_FOUND.getMessage());
    }

    @Test
    void getAllIssues() {
        // given
        Issue firstIssue = new Issue();
        firstIssue.setIssueId("TEST-1");
        firstIssue.setTitle("Test title");
        firstIssue.setDescription("Test description");
        firstIssue.setStatus("OPEN");

        Issue secondIssue = new Issue();
        secondIssue.setIssueId("TEST-2");
        secondIssue.setTitle("Test title");
        secondIssue.setDescription("Test description");
        secondIssue.setStatus("OPEN");

        List<Issue> issues = List.of(firstIssue, secondIssue);

        // when
        when(issueRepository.findAll()).thenReturn(issues);

        // then
        assertThat(issueService.getAllIssues()).isEqualTo(issues);
    }

    @Test
    void getAllIssues_Page() {
        // given
        int pageNo = 0;
        int pageSize = 10;

        Issue firstIssue = new Issue();
        firstIssue.setIssueId("TEST-1");
        firstIssue.setTitle("Test title");
        firstIssue.setDescription("Test description");
        firstIssue.setStatus("OPEN");

        Issue secondIssue = new Issue();
        secondIssue.setIssueId("TEST-2");
        secondIssue.setTitle("Test title");
        secondIssue.setDescription("Test description");
        secondIssue.setStatus("OPEN");

        List<Issue> issues = List.of(firstIssue, secondIssue);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Issue> page = new PageImpl<>(issues, pageable, issues.size());

        // when
        when(issueRepository.findAll(pageable)).thenReturn(page);

        // then
        Page<Issue> result = issueService.getAllIssues(pageNo, pageSize);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getContent()).isEqualTo(issues);
    }

    @Test
    void getIssuesByProjectKey_ExistingProject() {
        // given
        String projectKey = "TEST";

        Issue firstIssue = new Issue();
        firstIssue.setIssueId("TEST-1");
        firstIssue.setTitle("Test title");
        firstIssue.setDescription("Test description");
        firstIssue.setStatus("OPEN");

        Issue secondIssue = new Issue();
        secondIssue.setIssueId("TEST-2");
        secondIssue.setTitle("Test title");
        secondIssue.setDescription("Test description");
        secondIssue.setStatus("OPEN");

        List<Issue> issues = List.of(firstIssue, secondIssue);

        // when
        when(projectRepository.existsByProjectKey(projectKey)).thenReturn(true);
        when(issueRepository.findIssuesByProjectKey(projectKey)).thenReturn(issues);

        // then
        assertThat(issueService.getIssuesByProjectKey(projectKey)).isEqualTo(issues);
    }

    @Test
    void getIssuesByProjectKey_NonExistingProject() {
        // given
        String projectKey = "TEST";

        // when
        when(projectRepository.existsByProjectKey(projectKey)).thenReturn(false);

        // then
        assertThatThrownBy(() -> issueService.getIssuesByProjectKey(projectKey))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage(ExceptionMessages.PROJECT_NOT_FOUND.getMessage());
    }

    @Test
    void getIssueByIssueId_ExistingIssue() {
        // given
        String issueId = "TEST-1";

        Issue issue = new Issue();
        issue.setIssueId(issueId);
        issue.setTitle("Test title");
        issue.setDescription("Test description");
        issue.setStatus("OPEN");

        // when
        when(issueRepository.findIssueByIssueId(issueId)).thenReturn(java.util.Optional.of(issue));

        // then
        assertThat(issueService.getIssueByIssueId(issueId)).isEqualTo(issue);
    }

    @Test
    void getIssueByIssueId_NonExistingIssue() {
        // given
        String issueId = "TEST-1";

        // when
        when(issueRepository.findIssueByIssueId(issueId)).thenReturn(java.util.Optional.empty());

        // then
        assertThatThrownBy(() -> issueService.getIssueByIssueId(issueId))
                .isInstanceOf(IssueNotFoundException.class)
                .hasMessage("Issue not found");
    }

    @Test
    void getIssueByIssueIdAndProjectKey_ExistingIssue() {
        // given
        String issueId = "TEST-1";
        String projectKey = "TEST";

        Issue issue = new Issue();
        issue.setIssueId(issueId);
        issue.setTitle("Test title");
        issue.setDescription("Test description");
        issue.setStatus("OPEN");

        // when
        when(issueRepository.findIssueByIssueIdAndProjectKey(issueId, projectKey))
                .thenReturn(java.util.Optional.of(issue));

        // then
        assertThat(issueService.getIssueByIssueIdAndProjectKey(issueId, projectKey)).isEqualTo(issue);
    }

    @Test
    void getIssueByIssueIdAndProjectKey_NonExistingIssue() {
        // given
        String issueId = "TEST-1";
        String projectKey = "TEST";

        // when
        when(issueRepository.findIssueByIssueIdAndProjectKey(issueId, projectKey))
                .thenReturn(java.util.Optional.empty());

        // then
        assertThatThrownBy(() -> issueService.getIssueByIssueIdAndProjectKey(issueId, projectKey))
                .isInstanceOf(IssueOrProjectNotFoundException.class)
                .hasMessage(ExceptionMessages.ISSUE_OR_PROJECT_NOT_FOUND.getMessage());
    }

    @Test
    void updateIssue_ExistingIssue() {
        // given
        String issueId = "TEST-1";

        Issue issue = new Issue();
        issue.setIssueId(issueId);
        issue.setTitle("Test title");
        issue.setDescription("Test description");
        issue.setStatus("OPEN");

        Issue existingIssue = new Issue();
        existingIssue.setIssueId(issueId);
        existingIssue.setTitle("Existing title");
        existingIssue.setDescription("Existing description");
        existingIssue.setStatus("OPEN");

        // when
        when(issueRepository.findIssueByIssueId(issueId)).thenReturn(java.util.Optional.of(existingIssue));

        issueService.updateIssue(issueId, issue);

        // then
        verify(issueRepository, times(1)).save(issueCaptor.capture());

        Issue capturedIssue = issueCaptor.getValue();

        assertThat(capturedIssue.getIssueId()).isEqualTo(issueId);
        assertThat(capturedIssue.getTitle()).isEqualTo("Test title");
        assertThat(capturedIssue.getDescription()).isEqualTo("Test description");
        assertThat(capturedIssue.getStatus()).isEqualTo("OPEN");
    }

    @Test
    void updateIssue_NonExistingIssue() {
        // given
        String issueId = "TEST-1";

        Issue issue = new Issue();
        issue.setIssueId(issueId);
        issue.setTitle("Test title");
        issue.setDescription("Test description");
        issue.setStatus("OPEN");

        // when
        when(issueRepository.findIssueByIssueId(issueId)).thenReturn(java.util.Optional.empty());

        // then
        assertThatThrownBy(() -> issueService.updateIssue(issueId, issue))
                .isInstanceOf(IssueNotFoundException.class)
                .hasMessage(ExceptionMessages.ISSUE_NOT_FOUND.getMessage());
    }

    @Test
    void partialUpdateIssue_ExistingIssue() {
        // given
        String issueId = "TEST-1";

        Issue existingIssue = new Issue();
        existingIssue.setIssueId(issueId);
        existingIssue.setTitle("Existing title");
        existingIssue.setDescription("Existing description");
        existingIssue.setStatus("OPEN");

        // when
        when(issueRepository.findIssueByIssueId(issueId)).thenReturn(java.util.Optional.of(existingIssue));

        issueService.partialUpdateIssue(issueId, java.util.Map.of("title", "Test title"));

        // then
        verify(issueRepository, times(1)).save(issueCaptor.capture());

        Issue capturedIssue = issueCaptor.getValue();

        assertThat(capturedIssue.getIssueId()).isEqualTo(issueId);
        assertThat(capturedIssue.getTitle()).isEqualTo("Test title");
        assertThat(capturedIssue.getDescription()).isEqualTo("Existing description");
        assertThat(capturedIssue.getStatus()).isEqualTo("OPEN");
    }

    @Test
    void partialUpdateIssue_NonExistingIssue() {
        // given
        String issueId = "TEST-1";
        Map<String, Object> updates = Map.of("title", "Test title");

        // when
        when(issueRepository.findIssueByIssueId(issueId)).thenReturn(java.util.Optional.empty());

        // then
        assertThatThrownBy(() -> issueService.partialUpdateIssue(issueId, updates))
                .isInstanceOf(IssueNotFoundException.class)
                .hasMessage(ExceptionMessages.ISSUE_NOT_FOUND.getMessage());
    }

    @Test
    void partialUpdateIssue_ExistingIssue_InvalidField() {
        // given
        String issueId = "TEST-1";
        Map<String, Object> updates = Map.of("invalid", "Test title");

        Issue existingIssue = new Issue();
        existingIssue.setIssueId(issueId);
        existingIssue.setTitle("Existing title");
        existingIssue.setDescription("Existing description");
        existingIssue.setStatus("OPEN");

        // when
        when(issueRepository.findIssueByIssueId(issueId)).thenReturn(java.util.Optional.of(existingIssue));

        // then
        assertThatThrownBy(() -> issueService.partialUpdateIssue(issueId, updates))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid field: invalid");
    }

    @Test
    void deleteIssue() {
        // given
        String issueId = "TEST-1";

        // when
        issueService.deleteIssue(issueId);

        // then
        verify(issueRepository, times(1)).deleteByIssueId(issueId);
    }
}
