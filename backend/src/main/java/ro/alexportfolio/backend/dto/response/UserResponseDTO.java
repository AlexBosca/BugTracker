package ro.alexportfolio.backend.dto.response;

import ro.alexportfolio.backend.model.GlobalRole;

import java.time.LocalDateTime;

public record UserResponseDTO(
        String userId,
        String firstName,
        String lastName,
        String email,
        GlobalRole globalRole,
        LocalDateTime createdAt
) {
}
