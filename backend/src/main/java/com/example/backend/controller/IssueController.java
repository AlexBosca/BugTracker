package com.example.backend.controller;

import com.example.backend.dto.request.IssueCommentRequest;
import com.example.backend.dto.request.IssueRequest;
import com.example.backend.dto.response.IssueFullResponse;
import com.example.backend.enums.IssueStatus;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.IssueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.backend.enums.IssueStatus.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping(path = "issue")
@AllArgsConstructor
public class IssueController {

    @Autowired
    private final IssueService issueService;
    @Autowired
    private final MapStructMapper mapper;

    @GetMapping(path = "/all")
    public ResponseEntity<List<IssueFullResponse>> getAllIssues(){
        log.info("Get all issues");

        return new ResponseEntity<>(
                mapper.toResponses(issueService.getAllIssues()),
                OK
        );
    }

    @GetMapping(path = "/{issueId}")
    public ResponseEntity<IssueFullResponse> getIssue(@PathVariable(name = "issueId") String issueId){
        log.info("Get issue with id: {}", issueId);

        return new ResponseEntity<>(
                mapper.toResponse(issueService.getIssueByIssueId(issueId)),
                OK
        );
    }

    @PostMapping(path = "/createOnProject/{projectId}")
    public ResponseEntity<Void> createIssue(@RequestBody IssueRequest request,
                            @PathVariable(name = "projectId") String projectId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        log.info("Create new issue");

        issueService.saveIssue(mapper.toEntity(request), projectId, email);

        return new ResponseEntity<>(CREATED);
    }

    @PutMapping(path = "/{issueId}/assignToDeveloper/{developerId}")
    public ResponseEntity<Void> assignToDeveloper(@PathVariable(name = "issueId") String issueId,
                                       @PathVariable(name = "developerId") String developerId) {
        log.info("Assign issue with id: {} to developer with id: {}", issueId, developerId);

        issueService.assignToUser(issueId, developerId);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/open")
    public ResponseEntity<Void> open(@PathVariable(name = "issueId") String issueId) {
        log.info("Open issue with id: {} into state: {}", issueId, OPEN.name());

        issueService.changeIssueStatus(issueId, OPEN);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/fix")
    public ResponseEntity<Void> fix(@PathVariable(name = "issueId") String issueId) {
        log.info("Open issue with id: {} into state: {}", issueId, FIXED.name());

        issueService.changeIssueStatus(issueId, FIXED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/sendToRetest")
    public ResponseEntity<Void> sendToRetest(@PathVariable(name = "issueId") String issueId) {
        log.info("Open issue with id: {} into state: {}", issueId, PENDING_RETEST.name());

        issueService.changeIssueStatus(issueId, PENDING_RETEST);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/retest")
    public ResponseEntity<Void> retest(@PathVariable(name = "issueId") String issueId) {
        log.info("Open issue with id: {} into state: {}", issueId, RETEST.name());

        issueService.changeIssueStatus(issueId, RETEST);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/reopen")
    public ResponseEntity<Void> reopen(@PathVariable(name = "issueId") String issueId) {
        log.info("Open issue with id: {} into state: {}", issueId, REOPENED.name());

        issueService.changeIssueStatus(issueId, REOPENED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/verify")
    public ResponseEntity<Void> verify(@PathVariable(name = "issueId") String issueId) {
        log.info("Open issue with id: {} into state: {}", issueId, VERIFIED.name());

        issueService.changeIssueStatus(issueId, VERIFIED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/close")
    public ResponseEntity<Void> close(@PathVariable(name = "issueId") String issueId) {
        log.info("Open issue with id: {} into state: {}", issueId, CLOSED.name());

        issueService.changeIssueStatus(issueId, CLOSED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/duplicate")
    public ResponseEntity<Void> duplicate(@PathVariable(name = "issueId") String issueId) {
        log.info("Open issue with id: {} into state: {}", issueId, DUPLICATE.name());

        issueService.changeIssueStatus(issueId, DUPLICATE);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/reject")
    public ResponseEntity<Void> reject(@PathVariable(name = "issueId") String issueId) {
        log.info("Open issue with id: {} into state: {}", issueId, REJECTED.name());

        issueService.changeIssueStatus(issueId, REJECTED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/defer")
    public ResponseEntity<Void> defer(@PathVariable(name = "issueId") String issueId) {
        log.info("Open issue with id: {} into state: {}", issueId, DEFERRED.name());

        issueService.changeIssueStatus(issueId, DEFERRED);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{issueId}/notABug")
    public ResponseEntity<Void> notABug(@PathVariable(name = "issueId") String issueId) {
        log.info("Open issue with id: {} into state: {}", issueId, NOT_A_BUG.name());

        issueService.changeIssueStatus(issueId, NOT_A_BUG);

        return new ResponseEntity<>(OK);
    }

    @PostMapping(path = "/{issueId}/comment")
    public ResponseEntity<Void> createCommentOnIssue(@RequestBody IssueCommentRequest commentRequest,
                                                     @PathVariable(name = "issueId") String issueId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        log.info("User with id: {} create a comment on issue with id: {}", email, issueId);

        issueService.addComment(mapper.toEntity(commentRequest), issueId, email);

        return new ResponseEntity<>(CREATED);
    }
}
