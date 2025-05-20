package ro.alexportfolio.backend.dto.request;

import java.util.List;

public record UserProjectBatchAssignmentRequest(
    List<UserProjectAssignmentRequest> userProjectAssignments
) {}
