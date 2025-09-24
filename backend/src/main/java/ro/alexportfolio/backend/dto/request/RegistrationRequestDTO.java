package ro.alexportfolio.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDTO(
    @NotBlank(message = "Firstname is mandatory")
    String firstName,

    @NotBlank(message = "Lastname is mandatory")
    String lastName,

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    String email,

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    String password,

    @NotBlank(message = "Confirmed password is mandatory")
    @Size(min = 8, message = "Confirmed password must be at least 8 characters long")
    String confirmPassword
) { }
