package ro.alexportfolio.backend.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ro.alexportfolio.backend.dao.IssueRepository;
import ro.alexportfolio.backend.dao.ProjectRepository;
import ro.alexportfolio.backend.exception.IssueNotFoundException;
import ro.alexportfolio.backend.exception.IssueOrProjectNotFoundException;
import ro.alexportfolio.backend.exception.ProjectNotFoundException;
import ro.alexportfolio.backend.model.EmailData;
import ro.alexportfolio.backend.model.Issue;
import ro.alexportfolio.backend.util.EmailConstants;
import ro.alexportfolio.backend.util.Patcher;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final Clock clock;
    private final EmailSenderService emailSenderService;

    @Value("${application.name}")
    private String applicationName;

    public IssueService(IssueRepository issueRepository,
                        ProjectRepository projectRepository,
                        Clock clock,
                        @Qualifier("notification") EmailSenderService emailSenderService) {
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.clock = clock;
        this.emailSenderService = emailSenderService;
    }

    public void createIssue(String projectKey, Issue issue) {
        if(!projectRepository.existsByProjectKey(projectKey)) {
            throw new ProjectNotFoundException();
        }
        
        issue.setProjectKey(projectKey);
        issue.setCreatedAt(LocalDateTime.now(clock));
        issue.setUpdatedAt(LocalDateTime.now(clock));
        issueRepository.save(issue);
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public Page<Issue> getAllIssues(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return issueRepository.findAll(pageable);
    }

    public List<Issue> getIssuesByProjectKey(String projectKey) {
        if(!projectRepository.existsByProjectKey(projectKey)) {
            throw new ProjectNotFoundException();
        }

        return issueRepository.findIssuesByProjectKey(projectKey);
    }

    public Issue getIssueByIssueId(String issueId) {
        return issueRepository.findIssueByIssueId(issueId)
                .orElseThrow(IssueNotFoundException::new);
    }

    public Issue getIssueByIssueIdAndProjectKey(String issueId, String projectKey) {
        return issueRepository.findIssueByIssueIdAndProjectKey(issueId, projectKey)
                .orElseThrow(IssueOrProjectNotFoundException::new);
    }

    public void updateIssue(String issueId, Issue issue) {
        Issue existingIssue = issueRepository.findIssueByIssueId(issueId)
                .orElseThrow(IssueNotFoundException::new);

        existingIssue.setTitle(issue.getTitle());
        existingIssue.setDescription(issue.getDescription());
        issue.setUpdatedAt(LocalDateTime.now(clock));

        issueRepository.save(existingIssue);
    }

    public void partialUpdateIssue(String issueId, Map<String, Object> updates) {
        Issue existingIssue = issueRepository.findIssueByIssueId(issueId)
                .orElseThrow(IssueNotFoundException::new);

        Patcher.patch(existingIssue, updates);

        existingIssue.setUpdatedAt(LocalDateTime.now(clock));
        issueRepository.save(existingIssue);

        EmailData emailData = EmailData.builder()
                .recipientName(existingIssue.getAssignedUser().getFullName())
                .recipientEmail(existingIssue.getAssignedUser().getEmail())
                .subject(EmailConstants.EMAIL_ISSUE_UPDATES_SUBJECT.getValue())
                .title(EmailConstants.EMAIL_ISSUE_UPDATES_TITLE.getValue())
                .applicationName(applicationName)
                .confirmationLink(Optional.empty())
                .notificationContent(Optional.of(EmailConstants.EMAIL_ISSUE_UPDATES_CONTENT.getValue(existingIssue.getIssueId(), existingIssue.getTitle())))
                .build();

        emailSenderService.sendEmail(emailData);
    }

    public void deleteIssue(String issueId) {
        issueRepository.deleteByIssueId(issueId);
    }
}
