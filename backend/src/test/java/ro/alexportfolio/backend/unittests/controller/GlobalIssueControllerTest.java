package ro.alexportfolio.backend.unittests.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ro.alexportfolio.backend.controller.GlobalIssueController;
import ro.alexportfolio.backend.dto.response.IssueResponseDTO;
import ro.alexportfolio.backend.dto.response.PageResponse;
import ro.alexportfolio.backend.mapper.IssueMapper;
import ro.alexportfolio.backend.model.Issue;
import ro.alexportfolio.backend.service.IssueService;

@ExtendWith(MockitoExtension.class)
class GlobalIssueControllerTest {

    @Mock
    private IssueService issueService;
    
    @Mock
    private IssueMapper mapper;

    @InjectMocks
    private GlobalIssueController globalIssueController;

    @Test
    void testGetAllIssues() {
        Issue firstIssue = new Issue();
        firstIssue.setIssueId("TEST-1");
        firstIssue.setTitle("Test title");
        firstIssue.setDescription("Test description");
        firstIssue.setStatus("OPEN");

        Issue secondIssue = new Issue();
        secondIssue.setIssueId("TEST-2");
        secondIssue.setTitle("Test title");
        secondIssue.setDescription("Test description");
        secondIssue.setStatus("OPEN");

        List<Issue> issueList = List.of(firstIssue, secondIssue);

        IssueResponseDTO firstIssueResponseDTO = new IssueResponseDTO("TEST-1", "Test title", "Test description", null);
        IssueResponseDTO secondIssueResponseDTO = new IssueResponseDTO("TEST-2", "Test title", "Test description", null);

        List<IssueResponseDTO> issueResponseDTOList = List.of(firstIssueResponseDTO, secondIssueResponseDTO);
        when(issueService.getAllIssues()).thenReturn(issueList);
        when(mapper.toResponseList(issueList)).thenReturn(issueResponseDTOList);
        
        ResponseEntity<List<IssueResponseDTO>> responseEntity = globalIssueController.getAllIssues();

        assertThat(responseEntity.getBody()).isEqualTo(issueResponseDTOList);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAllIssuesPage() {
        Issue firstIssue = new Issue();
        firstIssue.setIssueId("TEST-1");
        firstIssue.setTitle("Test title");
        firstIssue.setDescription("Test description");
        firstIssue.setStatus("OPEN");

        Issue secondIssue = new Issue();
        secondIssue.setIssueId("TEST-2");
        secondIssue.setTitle("Test title");
        secondIssue.setDescription("Test description");
        secondIssue.setStatus("OPEN");

        List<Issue> issueList = List.of(firstIssue, secondIssue);
        Page<Issue> issuePage = new PageImpl<>(issueList, PageRequest.of(0, 10), 2);

        IssueResponseDTO firstIssueResponseDTO = new IssueResponseDTO("TEST-1", "Test title", "Test description", null);
        IssueResponseDTO secondIssueResponseDTO = new IssueResponseDTO("TEST-2", "Test title", "Test description", null);

        List<IssueResponseDTO> issueResponseDTOList = List.of(firstIssueResponseDTO, secondIssueResponseDTO);
        when(issueService.getAllIssues(0, 10)).thenReturn(issuePage);
        when(mapper.toResponse(firstIssue)).thenReturn(firstIssueResponseDTO);
        when(mapper.toResponse(secondIssue)).thenReturn(secondIssueResponseDTO);
        
        ResponseEntity<PageResponse<IssueResponseDTO>> responseEntity = globalIssueController.getAllIssues(0, 10);

        assertThat(responseEntity.getBody().getContent()).isEqualTo(issueResponseDTOList);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetIssue_ExistingIssue() {
        Issue issue = new Issue();
        issue.setIssueId("TEST-1");
        issue.setTitle("Test title");
        issue.setDescription("Test description");
        issue.setStatus("OPEN");

        IssueResponseDTO issueResponseDTO = new IssueResponseDTO("TEST-1", "Test title", "Test description", null);
        when(issueService.getIssueByIssueId("TEST-1")).thenReturn(issue);
        when(mapper.toResponse(issue)).thenReturn(issueResponseDTO);
        
        ResponseEntity<IssueResponseDTO> responseEntity = globalIssueController.getIssue("TEST-1");

        assertThat(responseEntity.getBody()).isEqualTo(issueResponseDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}