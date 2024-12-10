package ro.alexportfolio.backend.dto.response;

import java.time.LocalDateTime;

public record IssueResponseDTO(
        String issueId,
        String title,
        String description,
        LocalDateTime createdAt
) {
}
