package ro.alexportfolio.backend.unittests.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ro.alexportfolio.backend.controller.IssueController;
import ro.alexportfolio.backend.dto.request.IssueRequestDTO;
import ro.alexportfolio.backend.dto.response.IssueResponseDTO;
import ro.alexportfolio.backend.mapper.IssueMapper;
import ro.alexportfolio.backend.mapper.RecordMapper;
import ro.alexportfolio.backend.model.Issue;
import ro.alexportfolio.backend.service.IssueService;

@ExtendWith(MockitoExtension.class)
class IssueControllerTest {

    @Mock
    private IssueService issueService;

    @Mock
    private IssueMapper mapper;
    
    @InjectMocks
    private IssueController issueController;

    @Test
    void testCreateIssue() {
        IssueRequestDTO request = new IssueRequestDTO("TEST-1", "Test title", "Test description", "OPEN");
        Issue issue = new Issue();
        issue.setIssueId("TEST-1");
        issue.setTitle("Test title");
        issue.setDescription("Test description");
        issue.setStatus("OPEN");
        
        when(mapper.toEntity(request)).thenReturn(issue);
        doNothing().when(issueService).createIssue("TEST", issue);

        ResponseEntity<Void> responseEntity = issueController.createIssue("TEST", request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(issueService).createIssue("TEST", issue);
    }

    @Test
    void getAllIssues() {
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

        when(issueService.getIssuesByProjectKey("TEST")).thenReturn(List.of(firstIssue, secondIssue));
        
        IssueResponseDTO firstIssueResponseDTO = new IssueResponseDTO("TEST-1", "Test title", "Test description", null);
        IssueResponseDTO secondIssueResponseDTO = new IssueResponseDTO("TEST-2", "Test title", "Test description", null);
        when(mapper.toResponseList(List.of(firstIssue, secondIssue))).thenReturn(List.of(firstIssueResponseDTO, secondIssueResponseDTO));

        ResponseEntity<List<IssueResponseDTO>> responseEntity = issueController.getAllIssues("TEST");

        assertThat(responseEntity.getBody()).isEqualTo(List.of(firstIssueResponseDTO, secondIssueResponseDTO));
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetIssue_ExistingIssue() {
        Issue issue = new Issue();
        issue.setIssueId("TEST-1");
        issue.setTitle("Test title");
        issue.setDescription("Test description");
        issue.setStatus("OPEN");

        when(issueService.getIssueByIssueIdAndProjectKey("TEST-1", "TEST")).thenReturn(issue);
        
        IssueResponseDTO issueResponseDTO = new IssueResponseDTO("TEST-1", "Test title", "Test description", null);
        when(mapper.toResponse(issue)).thenReturn(issueResponseDTO);

        ResponseEntity<IssueResponseDTO> responseEntity = issueController.getIssue("TEST", "TEST-1");

        assertThat(responseEntity.getBody()).isEqualTo(issueResponseDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // @Test
    // void testGetIssue_NonExistingIssue() {
    //     when(issueService.getIssueByIssueIdAndProjectKey("TEST-1", "TEST")).thenThrow(new IllegalStateException("Issue not found"));

    //     ResponseEntity<IssueResponseDTO> responseEntity = issueController.getIssue("TEST", "TEST-1");

    //     assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    @Test
    void testUpdateIssue() {
        IssueRequestDTO request = new IssueRequestDTO("TEST-1", "Test title", "Test description", "OPEN");
        Issue issue = new Issue();
        issue.setIssueId("TEST-1");
        issue.setTitle("Test title");
        issue.setDescription("Test description");
        issue.setStatus("OPEN");
        
        when(mapper.toEntity(request)).thenReturn(issue);
        doNothing().when(issueService).updateIssue("TEST-1", issue);

        ResponseEntity<String> responseEntity = issueController.updateIssue("TEST", "TEST-1", request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(issueService).updateIssue("TEST-1", issue);
    }

    @Test
    void testPartialUpdateIssue() {
        IssueRequestDTO request = new IssueRequestDTO("TEST-1", "Test title", "Test description", "OPEN");
        Issue issue = new Issue();
        issue.setIssueId("TEST-1");
        issue.setTitle("Test title");
        issue.setDescription("Test description");
        issue.setStatus("OPEN");
        
        doNothing().when(issueService).partialUpdateIssue("TEST-1", RecordMapper.toMap(request));

        ResponseEntity<String> responseEntity = issueController.partialUpdateIssue("TEST", "TEST-1", request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(issueService).partialUpdateIssue("TEST-1", RecordMapper.toMap(request));
    }

    @Test
    void testDeleteIssue() {
        doNothing().when(issueService).deleteIssue("TEST-1");

        ResponseEntity<String> responseEntity = issueController.deleteIssue("TEST", "TEST-1");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(issueService).deleteIssue("TEST-1");
    }
}
