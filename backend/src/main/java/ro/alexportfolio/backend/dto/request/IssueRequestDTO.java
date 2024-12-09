package ro.alexportfolio.backend.dto.request;

public record IssueRequestDTO(
        String issueId,
        String title,
        String description
) {
}
