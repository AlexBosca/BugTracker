package com.example.backend.service;

import com.example.backend.dao.IssueCommentRepository;
import com.example.backend.dao.IssueRepository;
import com.example.backend.dao.ProjectRepository;
import com.example.backend.dao.UserRepository;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueCommentEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.enums.IssueStatus;
import com.example.backend.exception.issue.IssueIdNotFoundException;
import com.example.backend.exception.project.ProjectIdNotFoundException;
import com.example.backend.exception.user.UserEmailNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;

import static com.example.backend.enums.IssueStatus.NEW;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
@AllArgsConstructor
public class IssueService {

//    private final static String ISSUE_NOT_FOUND_MESSAGE = "Issue with id %s not found";
//    private final static String PROJECT_NOT_FOUND_MESSAGE = "Project with id %s not found";
//
//    private final static String USER_WITH_ID_NOT_FOUND_MESSAGE = "User with id %s not found";
//    private final static String USER_WITH_EMAIL_NOT_FOUND_MESSAGE = "User with email %s not found";

    private final IssueRepository issueRepository;
    private final IssueCommentRepository commentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    public List<IssueEntity> getAllIssues() {
        log.info("Return all issues");

        return issueRepository
                .findAll();
    }
    public IssueEntity getIssueByIssueId(String issueId) {
        log.info("Return issue with id: {}", issueId);

        return findIssue(issueId);
    }

    public void saveIssue(IssueEntity issue, String projectId, String email) {
        ProjectEntity project = findProject(projectId);

        UserEntity user = findUserByEmail(email);

        issue.setStatus(NEW);
        issue.setProject(project);
        issue.setCreatedByUser(user);
        issue.setCreatedOn(now(clock));

        log.info("Save issue on project with id: {}", projectId);

        issueRepository.save(issue);
    }

    public void assignToUser(String issueId, String developerId) {
        IssueEntity issue = findIssue(issueId);

        UserEntity developer = findUserByUserId(developerId);

        issue.setAssignedUser(developer);
        issue.setAssignedOn(now(clock));

        // TODO: com/example/backend/dao/ProjectRepository.java:14
        issueRepository.save(issue);
    }

    public void changeIssueStatus(String issueId, IssueStatus status) {
        IssueEntity issue = findIssue(issueId);
        IssueStatus currentStatus = issue.getStatus();

        issue.setStatus(currentStatus.getNextState(status));

        // TODO: com/example/backend/dao/ProjectRepository.java:14
        issueRepository.save(issue);
    }

    public void addComment(IssueCommentEntity issueComment, String issueId, String email) {
        IssueEntity issue = findIssue(issueId);
        UserEntity user = findUserByEmail(email);

        issueComment.setCreatedOnIssue(issue);
        issueComment.setCreatedOn(now(clock));
        issueComment.setCreatedByUser(user);

        commentRepository.save(issueComment);
    }

    public IssueEntity findIssue(String issueId) {
        return issueRepository
                .findByIssueId(issueId)
                .orElseThrow(() -> new IssueIdNotFoundException(issueId));
    }

    public UserEntity findUserByUserId(String userId) {
        return userRepository
                .findByUserId(userId)
                .orElseThrow(() -> new UserIdNotFoundException(userId));
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));
    }

    public ProjectEntity findProject(String projectId) {
        return projectRepository
                .findByProjectId(projectId)
                .orElseThrow(() -> new ProjectIdNotFoundException(projectId));
    }
}
