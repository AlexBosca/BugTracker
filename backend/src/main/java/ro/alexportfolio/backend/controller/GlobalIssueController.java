package ro.alexportfolio.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ro.alexportfolio.backend.dto.response.IssueResponseDTO;
import ro.alexportfolio.backend.dto.response.PageResponse;
import ro.alexportfolio.backend.mapper.IssueMapper;
import ro.alexportfolio.backend.service.IssueService;

@RestController
@RequestMapping("/issues")
public class GlobalIssueController {

    private final IssueService issueService;
    private final IssueMapper mapper;

    public GlobalIssueController(final IssueService issueServiceParam,
                                 final IssueMapper mapperParam) {
        this.issueService = issueServiceParam;
        this.mapper = mapperParam;
    }

    @GetMapping
    public ResponseEntity<List<IssueResponseDTO>> getAllIssues() {
        return new ResponseEntity<>(
                mapper.toResponseList(issueService.getAllIssues()),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/page")
    public ResponseEntity<PageResponse<IssueResponseDTO>> getAllIssues(final @RequestParam(name = "pageNo") int pageNo,
                                                                       final @RequestParam(name = "pageSize") int pageSize) {
        return new ResponseEntity<>(
                PageResponse.fromPage(issueService.getAllIssues(pageNo, pageSize).map(mapper::toResponse)),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{issueId}")
    public ResponseEntity<IssueResponseDTO> getIssue(final @PathVariable(name = "issueId") String issueId) {
        return new ResponseEntity<>(
                mapper.toResponse(issueService.getIssueByIssueId(issueId)),
                HttpStatus.OK
        );
    }
}
