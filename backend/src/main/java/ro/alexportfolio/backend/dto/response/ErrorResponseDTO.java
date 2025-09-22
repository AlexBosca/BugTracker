package ro.alexportfolio.backend.dto.response;

import java.time.Instant;

public record ErrorResponseDTO(int status, String message, Instant timeInstant) {
}
