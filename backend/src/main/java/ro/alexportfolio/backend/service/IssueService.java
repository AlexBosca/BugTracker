package ro.alexportfolio.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import ro.alexportfolio.backend.dao.IssueRepository;
import ro.alexportfolio.backend.model.Issue;
import ro.alexportfolio.backend.model.Project;
import ro.alexportfolio.backend.util.Patcher;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public void createIssue(Issue issue) {
        issue.setCreatedAt(LocalDateTime.now());
        issueRepository.save(issue);
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public Issue getIssueByIssueId(String issueId) {
        return issueRepository.findIssueByIssueId(issueId)
                .orElseThrow(() -> new IllegalStateException("Issue not found"));
    }

    public void updateIssue(String issueId, Issue issue) {
        Issue existingIssue = issueRepository.findIssueByIssueId(issueId)
                .orElseThrow(() -> new IllegalStateException("Issue not found"));

        existingIssue.setTitle(issue.getTitle());
        existingIssue.setDescription(issue.getDescription());

        issueRepository.save(existingIssue);
    }

    public void partialUpdateIssue(String issueId, Map<String, Object> updates) {
        Issue existingIssue = issueRepository.findIssueByIssueId(issueId)
                .orElseThrow(() -> new IllegalStateException("Issue not found"));

        Patcher.patch(existingIssue, updates);

        issueRepository.save(existingIssue);
    }

    public void deleteIssue(String issueId) {
        issueRepository.deleteByIssueId(issueId);
    }
}
