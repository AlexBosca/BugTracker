package ro.alexportfolio.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record IssueRequestDTO(
        @NotBlank
        String issueId,
        
        @NotBlank
        String title,
        
        @NotBlank
        String description,

        String projectKey,
        
        String status,

        String priority
) {
}
