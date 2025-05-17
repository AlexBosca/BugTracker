package ro.alexportfolio.backend.dto.request;

public record UserProjectAssignmentRequest(
    String userId,
    String roleName
) {}
