package com.example.backend.service;

import com.example.backend.dao.IssueCommentDao;
import com.example.backend.dao.IssueDao;
import com.example.backend.dao.ProjectDao;
import com.example.backend.dao.UserDao;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueCommentEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.enums.IssueStatus;
import com.example.backend.exception.issue.IssueAlreadyCreatedException;
import com.example.backend.exception.issue.IssueNotFoundException;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.exception.user.UserEmailNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;

import static com.example.backend.enums.IssueStatus.NEW;
import static com.example.backend.util.issue.IssueUtilities.*;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class IssueService {

    @Qualifier("issue-jpa")
    private final IssueDao issueDao;

    private final IssueCommentDao commentDao;
    
    private final ProjectDao projectDao;
    
    private final UserDao userDao;
    
    private final Clock clock;

    public List<IssueEntity> getAllIssues() {
        log.info(ISSUE_REQUEST_ALL);

        List<IssueEntity> issues = issueDao.selectAllIssues();

        log.info(ISSUE_RETURN_ALL);

        return issues;
    }
    public IssueEntity getIssueByIssueId(String issueId) {
        log.info(ISSUE_REQUEST_BY_ID, issueId);

        IssueEntity issue = issueDao
            .selectIssueByIssueId(issueId)
            .orElseThrow(() -> new IssueNotFoundException(issueId));

        log.info(ISSUE_RETURN);

        return issue;
    }

    public void saveIssue(IssueEntity issue, String projectKey, String email) {
        log.info(ISSUE_CREATE_ON_PROJECT, projectKey);

        ProjectEntity project = projectDao
            .selectProjectByKey(projectKey)
            .orElseThrow(() -> new ProjectNotFoundException(projectKey));

        UserEntity user = userDao
            .selectUserByEmail(email)
            .orElseThrow(() -> new UserEmailNotFoundException(email));

        boolean isIssuePresent = issueDao
                .existsIssueWithIssueId(issue.getIssueId());

        if(isIssuePresent) {
            throw new IssueAlreadyCreatedException(issue.getIssueId());
        }

        issue.setStatus(NEW);
        issue.setProject(project);
        issue.setCreatedByUser(user);
        issue.setCreatedOn(now(clock));

        issueDao.insertIssue(issue);

        log.info(ISSUE_CREATED);
    }

    public void assignToUser(String issueId, String assigneeId) {
        log.info(ISSUE_ASSIGN_BY_ID_TO_DEV, issueId, assigneeId);

        IssueEntity issue = issueDao
            .selectIssueByIssueId(issueId)
            .orElseThrow(() -> new IssueNotFoundException(issueId));

        UserEntity assignee = userDao
            .selectUserByUserId(assigneeId)
            .orElseThrow(() -> new UserIdNotFoundException(assigneeId));

        issue.setAssignedUser(assignee);
        issue.setAssignedOn(now(clock));

        // TODO: com/example/backend/dao/ProjectRepository.java:14
        issueDao.updateIssue(issue);

        log.info(ISSUE_ASSIGNED_TO_DEV);
    }

    public void closeByUser(String issueId, String developerEmail) {
        log.info(ISSUE_CLOSE_BY_EMAIL_BY_DEV, issueId, developerEmail);

        IssueEntity issue = issueDao
            .selectIssueByIssueId(issueId)
            .orElseThrow(() -> new IssueNotFoundException(issueId));

        UserEntity developer = userDao
            .selectUserByEmail(developerEmail)
            .orElseThrow(() -> new UserEmailNotFoundException(developerEmail));

        issue.setClosedByUser(developer);
        issue.setClosedOn(now(clock));

        // TODO: com/example/backend/dao/ProjectRepository.java:14
        issueDao.updateIssue(issue);

        log.info(ISSUE_CLOSE_BY_DEV);
    }

    public void changeIssueStatus(String issueId, IssueStatus status, String developerEmail) {
        log.info(ISSUE_CHANGE_STATUS_BY_ID, issueId, status);

        IssueEntity issue = issueDao
            .selectIssueByIssueId(issueId)
            .orElseThrow(() -> new IssueNotFoundException(issueId));

        UserEntity developer = userDao
            .selectUserByEmail(developerEmail)
            .orElseThrow(() -> new UserEmailNotFoundException(developerEmail));

        IssueStatus currentStatus = issue.getStatus();

        issue.setStatus(currentStatus.transitionTo(status));
        issue.setModifiedByUser(developer);
        issue.setModifiedOn(now(clock));

        // TODO: com/example/backend/dao/ProjectRepository.java:14
        issueDao.updateIssue(issue);

        log.info(ISSUE_CHANGED_STATUS);
    }

    public void addComment(IssueCommentEntity issueComment, String issueId, String email) {
        log.info(ISSUE_USER_ADD_COMMENT_BY_ID, email, issueId);
        
        IssueEntity issue = issueDao
            .selectIssueByIssueId(issueId)
            .orElseThrow(() -> new IssueNotFoundException(issueId));

        UserEntity user = userDao
                .selectUserByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));

        issueComment.setCreatedOnIssue(issue);
        issueComment.setCreatedOn(now(clock));
        issueComment.setCreatedByUser(user);

        commentDao.insertComment(issueComment);

        log.info(ISSUE_COMMENT_ADDED);
    }
}
