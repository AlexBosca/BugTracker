package ro.alexportfolio.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.alexportfolio.backend.dto.request.IssueRequestDTO;
import ro.alexportfolio.backend.dto.response.IssueResponseDTO;
import ro.alexportfolio.backend.mapper.IssueMapper;
import ro.alexportfolio.backend.mapper.RecordMapper;
import ro.alexportfolio.backend.service.IssueService;

import java.util.List;

@RestController
@RequestMapping("/projects/{projectKey}/issues")
public class IssueController {

    private final IssueService issueService;
    private final IssueMapper mapper;

    public IssueController(final IssueService issueServiceParam,
                           final IssueMapper mapperParam) {
        this.issueService = issueServiceParam;
        this.mapper = mapperParam;
    }

    @PostMapping
    public ResponseEntity<Void> createIssue(final @PathVariable(name = "projectKey") String projectKey,
                                            final @RequestBody IssueRequestDTO request) {
        issueService.createIssue(projectKey, mapper.toEntity(request));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IssueResponseDTO>> getAllIssues(final @PathVariable(name = "projectKey") String projectKey) {
        return new ResponseEntity<>(
                mapper.toResponseList(issueService.getIssuesByProjectKey(projectKey)),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{issueId}")
    public ResponseEntity<IssueResponseDTO> getIssue(final @PathVariable(name = "projectKey") String projectKey,
                                                     final @PathVariable(name = "issueId") String issueId) {
        return new ResponseEntity<>(
                mapper.toResponse(issueService.getIssueByIssueIdAndProjectKey(issueId, projectKey)),
                HttpStatus.OK
        );
    }

    @PutMapping(path = "/{issueId}")
    public ResponseEntity<String> updateIssue(final @PathVariable(name = "projectKey") String projectKey,
                                              final @PathVariable(name = "issueId") String issueId,
                                              final @RequestBody IssueRequestDTO request) {
        issueService.updateIssue(issueId, mapper.toEntity(request));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/{issueId}")
    public ResponseEntity<String> partialUpdateIssue(final @PathVariable(name = "projectKey") String projectKey,
                                                     final @PathVariable(name = "issueId") String issueId,
                                                     final @RequestBody IssueRequestDTO request) {
        issueService.partialUpdateIssue(issueId, RecordMapper.toMap(request));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{issueId}")
    public ResponseEntity<String> deleteIssue(final @PathVariable(name = "projectKey") String projectKey,
                                              final @PathVariable(name = "issueId") String issueId) {
        issueService.deleteIssue(issueId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
