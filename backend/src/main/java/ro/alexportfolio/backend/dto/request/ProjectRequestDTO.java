package ro.alexportfolio.backend.dto.request;

public record ProjectRequestDTO(
        String projectKey,
        String name,
        String description
) {
}
