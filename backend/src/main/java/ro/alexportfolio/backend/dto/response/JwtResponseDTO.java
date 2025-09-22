package ro.alexportfolio.backend.dto.response;

import java.util.Set;

public record JwtResponseDTO(
    String accessToken,
    String userId,
    String email,
    Set<String> roles
) { }
