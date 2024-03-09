package com.example.backend.service;

import com.example.backend.dao.IssueCommentDao;
import com.example.backend.dao.IssueDao;
import com.example.backend.dao.ProjectDao;
import com.example.backend.dao.UserDao;
import com.example.backend.dto.filter.FilterCriteria;
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
import com.example.backend.model.EmailData;
import com.example.backend.util.email.EmailConstants;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

import static com.example.backend.enums.IssueStatus.ASSIGNED;
import static com.example.backend.enums.IssueStatus.CLOSED;
import static com.example.backend.enums.IssueStatus.NEW;
import static com.example.backend.util.issue.IssueLoggingMessages.*;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
public class IssueService {
    private final IssueDao issueDao;
    private final IssueCommentDao commentDao;
    private final ProjectDao projectDao;
    private final UserDao userDao;
    private final Clock clock;
    private final EmailSenderService emailSenderService;

    @Value("${application.name}")
    private String applicationName;

    public IssueService(@Qualifier("issueJpa") IssueDao issueDao,
                        @Qualifier("commentJpa") IssueCommentDao commentDao,
                        ProjectDao projectDao,
                        UserDao userDao,
                        Clock clock,
                        @Qualifier("notification") EmailSenderService emailSenderService) {
        this.issueDao = issueDao;
        this.commentDao = commentDao;
        this.projectDao = projectDao;
        this.userDao = userDao;
        this.clock = clock;
        this.emailSenderService = emailSenderService;
    }

    public List<IssueEntity> getAllIssues() {
        List<IssueEntity> issues = issueDao.selectAllIssues();
        logInfo(ISSUES_ALL_RETRIEVED, issues);

        return issues;
    }

    public List<IssueEntity> filterIssues(FilterCriteria filterCriteria) {
        List<IssueEntity> issues = issueDao.selectAllFilteredIssues(filterCriteria);
        logInfo(ISSUES_FILTERED_RETRIEVED, issues);

        return issues;
    }

    public IssueEntity getIssueByIssueId(String issueId) {
        IssueEntity issue = issueDao
            .selectIssueByIssueId(issueId)
            .orElseThrow(() -> new IssueNotFoundException(issueId));

        logInfo(ISSUE_RETRIEVED, issue);

        return issue;
    }

    public void saveIssue(IssueEntity issue, String projectKey, String email) {
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
        logInfo(ISSUE_CREATED, issue);
    }

    public void assignToUser(String issueId, String assigneeId) {
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
        logInfo(ISSUE_STATUS_UPDATED, ASSIGNED);

        EmailData emailData = EmailData.builder()
            .recipientName(assignee.getFullName())
            .recipientEmail(assignee.getEmail())
            .subject(EmailConstants.EMAIL_ISSUE_ASSIGNATION_SUBJECT)
            .title(EmailConstants.EMAIL_ISSUE_ASSIGNATION_TITLE)
            .applicationName(applicationName)
            .confirmationLink(Optional.empty())
            .notificationContent(Optional.of(EmailConstants.EMAIL_ISSUE_ASSIGNATION_CONTENT))
            .build();

        emailSenderService.send(emailData);
    }

    public void closeByUser(String issueId, String developerEmail) {
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
        logInfo(ISSUE_STATUS_UPDATED, CLOSED);

        EmailData emailData = EmailData.builder()
            .recipientName(developer.getFullName())
            .recipientEmail(developer.getEmail())
            .subject(EmailConstants.EMAIL_ISSUE_CLOSING_SUBJECT)
            .title(EmailConstants.EMAIL_ISSUE_CLOSING_TITLE)
            .applicationName(applicationName)
            .confirmationLink(Optional.empty())
            .notificationContent(Optional.of(EmailConstants.EMAIL_ISSUE_CLOSING_CONTENT))
            .build();

        emailSenderService.send(emailData);
    }

    public void changeIssueStatus(String issueId, IssueStatus status, String developerEmail) {
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
        logInfo(ISSUE_STATUS_UPDATED, status);
    }

    public void addComment(IssueCommentEntity comment, String issueId, String email) {
        IssueEntity issue = issueDao
            .selectIssueByIssueId(issueId)
            .orElseThrow(() -> new IssueNotFoundException(issueId));

        UserEntity user = userDao
            .selectUserByEmail(email)
            .orElseThrow(() -> new UserEmailNotFoundException(email));

        comment.setCreatedOnIssue(issue);
        comment.setCreatedOn(now(clock));
        comment.setCreatedByUser(user);

        commentDao.insertComment(comment);
        logInfo(ISSUE_COMMENT_CREATED, comment);
    }

    private void logInfo(String var1, Object var2) {
        log.info(var1, "Service", var2);
    }
}
