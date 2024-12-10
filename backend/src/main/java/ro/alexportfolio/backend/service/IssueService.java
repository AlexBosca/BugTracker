package ro.alexportfolio.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.alexportfolio.backend.dao.IssueRepository;
import ro.alexportfolio.backend.model.Issue;
import ro.alexportfolio.backend.model.Project;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

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

    public void deleteIssue(String issueId) {
        issueRepository.deleteByIssueId(issueId);
    }
}
