package ro.alexportfolio.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ro.alexportfolio.backend.dao.IssueRepository;
import ro.alexportfolio.backend.model.Issue;
import ro.alexportfolio.backend.util.Patcher;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class IssueService {

    private static final String ISSUE_NOT_FOUND = "Issue not found";

    private final IssueRepository issueRepository;
    private final Clock clock;

    public IssueService(IssueRepository issueRepository, Clock clock) {
        this.issueRepository = issueRepository;
        this.clock = clock;
    }

    public void createIssue(Issue issue) {
        issue.setCreatedAt(LocalDateTime.now(clock));
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
        return issueRepository.findIssuesByProjectKey(projectKey);
    }

    public Issue getIssueByIssueId(String issueId) {
        return issueRepository.findIssueByIssueId(issueId)
                .orElseThrow(() -> new IllegalStateException(ISSUE_NOT_FOUND));
    }

    public Issue getIssueByIssueIdAndProjectKey(String issueId, String projectKey) {
        return issueRepository.findIssueByIssueIdAndProjectKey(issueId, projectKey)
                .orElseThrow(() -> new IllegalStateException(ISSUE_NOT_FOUND));
    }

    public void updateIssue(String issueId, Issue issue) {
        Issue existingIssue = issueRepository.findIssueByIssueId(issueId)
                .orElseThrow(() -> new IllegalStateException(ISSUE_NOT_FOUND));

        existingIssue.setTitle(issue.getTitle());
        existingIssue.setDescription(issue.getDescription());

        issueRepository.save(existingIssue);
    }

    public void partialUpdateIssue(String issueId, Map<String, Object> updates) {
        Issue existingIssue = issueRepository.findIssueByIssueId(issueId)
                .orElseThrow(() -> new IllegalStateException(ISSUE_NOT_FOUND));

        Patcher.patch(existingIssue, updates);

        issueRepository.save(existingIssue);
    }

    public void deleteIssue(String issueId) {
        issueRepository.deleteByIssueId(issueId);
    }
}
