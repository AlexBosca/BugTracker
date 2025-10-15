package ro.alexportfolio.backend.dto.request;

public record PasswordResetRequestDTO(
    String token,
    String newPassword
) { }
