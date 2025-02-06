package ro.alexportfolio.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    public IssueController(IssueService issueService, IssueMapper mapper) {
        this.issueService = issueService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<Void> createIssue(@PathVariable(name = "projectKey") String projectKey,
                                            @RequestBody IssueRequestDTO request) {
        issueService.createIssue(mapper.toEntity(request));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IssueResponseDTO>> getAllIssues(@PathVariable(name = "projectKey") String projectKey) {
        return new ResponseEntity<>(
                mapper.toResponseList(issueService.getIssuesByProjectKey(projectKey)),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{issueId}")
    public ResponseEntity<IssueResponseDTO> getIssue(@PathVariable(name = "projectKey") String projectKey,
                                                     @PathVariable(name = "issueId") String issueId) {
        return new ResponseEntity<>(
                mapper.toResponse(issueService.getIssueByIssueIdAndProjectKey(issueId, projectKey)),
                HttpStatus.OK
        );
    }

    @PutMapping(path = "/{issueId}")
    public ResponseEntity<String> updateIssue(@PathVariable(name = "projectKey") String projectKey,
                                              @PathVariable(name = "issueId") String issueId,
                                              @RequestBody IssueRequestDTO request) {
        issueService.updateIssue(issueId, mapper.toEntity(request));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/{issueId}")
    public ResponseEntity<String> partialUpdateIssue(@PathVariable(name = "projectKey") String projectKey,
                                                     @PathVariable(name = "issueId") String issueId,
                                                     @RequestBody IssueRequestDTO request) {
        issueService.partialUpdateIssue(issueId, RecordMapper.toMap(request));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{issueId}")
    public ResponseEntity<String> deleteIssue(@PathVariable(name = "projectKey") String projectKey,
                                              @PathVariable(name = "issueId") String issueId) {
        issueService.deleteIssue(issueId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
