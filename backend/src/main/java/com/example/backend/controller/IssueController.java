package com.example.backend.controller;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.IssueCommentRequest;
import com.example.backend.dto.request.IssueRequest;
import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.entity.issue.IssueCommentEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.AuthenticationService;
import com.example.backend.service.IssueService;
import com.example.backend.enums.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.backend.enums.IssueStatus.*;
import static com.example.backend.util.issue.IssueLoggingMessages.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping(path = "issues")
@AllArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final AuthenticationService authenticationService;
    private final MapStructMapper mapper;

    @GetMapping
    public ResponseEntity<List<IssueFullResponse>> getAllIssues() {
        List<IssueEntity> issues = issueService.getAllIssues();
        logInfo(ISSUES_ALL_RETRIEVED, issues);

        List<IssueFullResponse> issuesResponses = mapper.toResponses(issues);

        return new ResponseEntity<>(issuesResponses, OK);
    }

    @PostMapping(path = "/filter")
    public ResponseEntity<List<IssueFullResponse>> getFilteredIssues(@RequestBody FilterCriteria filterCriteria) {
        List<IssueEntity> issues = issueService.filterIssues(filterCriteria);
        logInfo(ISSUES_FILTERED_RETRIEVED, issues);

        List<IssueFullResponse> issuesResponses = mapper.toResponses(issues);

        return new ResponseEntity<>(issuesResponses, OK);
    }

    @GetMapping(path = "/{issueId}")
    public ResponseEntity<IssueFullResponse> getIssue(@PathVariable(name = "issueId") String issueId){
        IssueEntity issue = issueService.getIssueByIssueId(issueId);
        logInfo(ISSUE_RETRIEVED, issue);

        IssueFullResponse issueResponse = mapper.toResponse(issue);

        return new ResponseEntity<>(issueResponse, OK);
    }

    @PostMapping(path = "/createOnProject/{projectKey}")
    public ResponseEntity<Void> createIssue(@RequestBody IssueRequest request,
                                            @PathVariable(name = "projectKey") String projectKey) {
        String userEmail = getLoggedUserEmail();

        IssueEntity issue = mapper.toEntity(request);

        issueService.saveIssue(issue, projectKey, userEmail);
        logInfo(ISSUE_CREATED, issue);

        return new ResponseEntity<>(CREATED);
    }

    @PutMapping(path = "/{issueId}/assignToDeveloper/{assigneeId}")
    public ResponseEntity<Void> assignToDeveloper(@PathVariable(name = "issueId") String issueId,
                                                  @PathVariable(name = "assigneeId") String assigneeId) {
        issueService.assignToUser(issueId, assigneeId);
        changeStatusOfGivenIssue(issueId, ASSIGNED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/open")
    public ResponseEntity<Void> open(@PathVariable(name = "issueId") String issueId) {
        changeStatusOfGivenIssue(issueId, OPEN);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/fix")
    public ResponseEntity<Void> fix(@PathVariable(name = "issueId") String issueId) {
        changeStatusOfGivenIssue(issueId, FIXED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/sendToRetest")
    public ResponseEntity<Void> sendToRetest(@PathVariable(name = "issueId") String issueId) {
        changeStatusOfGivenIssue(issueId, PENDING_RETEST);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/retest")
    public ResponseEntity<Void> retest(@PathVariable(name = "issueId") String issueId) {
        changeStatusOfGivenIssue(issueId, RETEST);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/reopen")
    public ResponseEntity<Void> reopen(@PathVariable(name = "issueId") String issueId) {
        changeStatusOfGivenIssue(issueId, REOPENED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/verify")
    public ResponseEntity<Void> verify(@PathVariable(name = "issueId") String issueId) {
 
        changeStatusOfGivenIssue(issueId, VERIFIED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/closeByDeveloper")
    public ResponseEntity<Void> close(@PathVariable(name = "issueId") String issueId) {
        String developerEmail = getLoggedUserEmail();

        issueService.closeByUser(issueId, developerEmail);
        changeStatusOfGivenIssue(issueId, CLOSED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/duplicate")
    public ResponseEntity<Void> duplicate(@PathVariable(name = "issueId") String issueId) {
        changeStatusOfGivenIssue(issueId, DUPLICATE);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/reject")
    public ResponseEntity<Void> reject(@PathVariable(name = "issueId") String issueId) {
        changeStatusOfGivenIssue(issueId, REJECTED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/defer")
    public ResponseEntity<Void> defer(@PathVariable(name = "issueId") String issueId) {
        changeStatusOfGivenIssue(issueId, DEFERRED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/notABug")
    public ResponseEntity<Void> notABug(@PathVariable(name = "issueId") String issueId) {
        changeStatusOfGivenIssue(issueId, NOT_A_BUG);

        return new ResponseEntity<>(OK);
    }

    @PostMapping(path = "/{issueId}/comment")
    public ResponseEntity<Void> createCommentOnIssue(@RequestBody IssueCommentRequest commentRequest,
                                                     @PathVariable(name = "issueId") String issueId,
                                                     @RequestHeader("Authorization") String authorizationHeader) {
                                                        
        String email = authenticationService.getEmailFromAuthorizationHeader(authorizationHeader);

        IssueCommentEntity comment = mapper.toEntity(commentRequest);
        issueService.addComment(comment, issueId, email);
        logInfo(ISSUE_COMMENT_CREATED, comment);

        return new ResponseEntity<>(CREATED);
    }

    private void changeStatusOfGivenIssue(String issueId, IssueStatus status) {
        String developerEmail = getLoggedUserEmail();

        issueService.changeIssueStatus(issueId, status, developerEmail);

        logInfo(ISSUE_STATUS_UPDATED, status);
    }

    private void logInfo(String var1, Object var2) {
        log.info(var1, "Controller", var2);
    }

    private String getLoggedUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
