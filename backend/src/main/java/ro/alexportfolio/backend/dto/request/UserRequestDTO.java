package ro.alexportfolio.backend.dto.request;

import ro.alexportfolio.backend.model.GlobalRole;

public record UserRequestDTO(
        String userId,
        String firstName,
        String lastName,
        String email,
        String password,
        GlobalRole globalRole
) {
}
