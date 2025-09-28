package ro.alexportfolio.backend.dto.request;

public record LoginRequestDTO(
    String username,
    String password
) { }
