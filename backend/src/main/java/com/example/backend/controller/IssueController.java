package com.example.backend.controller;

import com.example.backend.dto.request.IssueCommentRequest;
import com.example.backend.dto.request.IssueRequest;
import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.AuthenticationService;
import com.example.backend.service.IssueService;
import com.example.backend.enums.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.backend.enums.IssueStatus.*;
import static com.example.backend.util.issue.IssueUtilities.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping(path = "issues")
@AllArgsConstructor
public class IssueController {

    @Autowired
    private final IssueService issueService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final MapStructMapper mapper;

    @GetMapping
    public ResponseEntity<List<IssueFullResponse>> getAllIssues(){
        log.info(ISSUE_GET_ALL);

        return new ResponseEntity<>(
                mapper.toResponses(issueService.getAllIssues()),
                OK
        );
    }

    @GetMapping(path = "/{issueId}")
    public ResponseEntity<IssueFullResponse> getIssue(@PathVariable(name = "issueId") String issueId){
        log.info(ISSUE_GET_BY_ID, issueId);

        return new ResponseEntity<>(
                mapper.toResponse(issueService.getIssueByIssueId(issueId)),
                OK
        );
    }

    @PostMapping(path = "/createOnProject/{projectKey}")
    public ResponseEntity<Void> createIssue(
            // @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody IssueRequest request,
            @PathVariable(name = "projectKey") String projectKey) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        // String email = authenticationService.getEmailFromAuthorizationHeader(authorizationHeader);

        log.info(ISSUE_CREATE);

        IssueEntity entity = mapper.toEntity(request);

        issueService.saveIssue(entity, projectKey, email);

        return new ResponseEntity<>(CREATED);
    }

    @PutMapping(path = "/{issueId}/assignToDeveloper/{assigneeId}")
    public ResponseEntity<Void> assignToDeveloper(@PathVariable(name = "issueId") String issueId,
                                       @PathVariable(name = "assigneeId") String assigneeId) {
        log.info(ISSUE_ASSIGN_TO_DEV);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String assignerEmail = auth.getName();

        issueService.assignToUser(issueId, assigneeId, assignerEmail);
        issueService.changeIssueStatus(issueId, ASSIGNED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/open")
    public ResponseEntity<Void> open(@PathVariable(name = "issueId") String issueId) {
        logIssueChangingState(issueId, OPEN);

        issueService.changeIssueStatus(issueId, OPEN);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/fix")
    public ResponseEntity<Void> fix(@PathVariable(name = "issueId") String issueId) {
        logIssueChangingState(issueId, FIXED);

        issueService.changeIssueStatus(issueId, FIXED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/sendToRetest")
    public ResponseEntity<Void> sendToRetest(@PathVariable(name = "issueId") String issueId) {
        logIssueChangingState(issueId, PENDING_RETEST);

        issueService.changeIssueStatus(issueId, PENDING_RETEST);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/retest")
    public ResponseEntity<Void> retest(@PathVariable(name = "issueId") String issueId) {
        logIssueChangingState(issueId, RETEST);

        issueService.changeIssueStatus(issueId, RETEST);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/reopen")
    public ResponseEntity<Void> reopen(@PathVariable(name = "issueId") String issueId) {
        logIssueChangingState(issueId, REOPENED);

        issueService.changeIssueStatus(issueId, REOPENED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/verify")
    public ResponseEntity<Void> verify(@PathVariable(name = "issueId") String issueId) {
        logIssueChangingState(issueId, VERIFIED);

        issueService.changeIssueStatus(issueId, VERIFIED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/closeByDeveloper/{developerId}")
    public ResponseEntity<Void> close(@PathVariable(name = "issueId") String issueId,
                                @PathVariable(name = "developerId") String developerId) {
        logIssueChangingState(issueId, CLOSED);

        issueService.closeByUser(issueId, developerId);
        issueService.changeIssueStatus(issueId, CLOSED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/duplicate")
    public ResponseEntity<Void> duplicate(@PathVariable(name = "issueId") String issueId) {
        logIssueChangingState(issueId, DUPLICATE);

        issueService.changeIssueStatus(issueId, DUPLICATE);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/reject")
    public ResponseEntity<Void> reject(@PathVariable(name = "issueId") String issueId) {
        logIssueChangingState(issueId, REJECTED);

        issueService.changeIssueStatus(issueId, REJECTED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/defer")
    public ResponseEntity<Void> defer(@PathVariable(name = "issueId") String issueId) {
        logIssueChangingState(issueId, DEFERRED);

        issueService.changeIssueStatus(issueId, DEFERRED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/notABug")
    public ResponseEntity<Void> notABug(@PathVariable(name = "issueId") String issueId) {
        logIssueChangingState(issueId, NOT_A_BUG);

        issueService.changeIssueStatus(issueId, NOT_A_BUG);

        return new ResponseEntity<>(OK);
    }

    @PostMapping(path = "/{issueId}/comment")
    public ResponseEntity<Void> createCommentOnIssue(@RequestBody IssueCommentRequest commentRequest,
                                                     @PathVariable(name = "issueId") String issueId,
                                                     @RequestHeader("Authorization") String authorizationHeader) {
                                                        
        String email = authenticationService.getEmailFromAuthorizationHeader(authorizationHeader);

        log.info("User with id: {} create a comment on issue with id: {}", email, issueId);

        issueService.addComment(mapper.toEntity(commentRequest), issueId, email);

        return new ResponseEntity<>(CREATED);
    }

    private void logIssueChangingState(String issueId, IssueStatus issueStatus) {
        log.info(ISSUE_CHANGE_STATE_MSG, issueId, issueStatus.name());
    }
}
