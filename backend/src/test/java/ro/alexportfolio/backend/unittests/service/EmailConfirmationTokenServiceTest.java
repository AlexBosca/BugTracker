package ro.alexportfolio.backend.unittests.service;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.alexportfolio.backend.dao.EmailConfirmationTokenRepository;
import ro.alexportfolio.backend.model.EmailConfirmationToken;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.service.EmailConfirmationTokenService;

@ExtendWith(MockitoExtension.class)
class EmailConfirmationTokenServiceTest {

    @Mock
    private EmailConfirmationTokenRepository confirmationTokenRepository;

    private EmailConfirmationTokenService confirmationTokenService;

    private String token;

    @BeforeEach
    void setUp() {
        token = UUID.randomUUID().toString();
        confirmationTokenService = new EmailConfirmationTokenService(confirmationTokenRepository);
    }

    @Test
    void getConfirmationTokenByToken_Success() {
        // Given
        User user = new User("userId", "Firstname", "Lastname", "firstname.lastname@email.com", "password", null);
        EmailConfirmationToken confirmationToken = new EmailConfirmationToken(token, user, null, false);

        // When
        when(confirmationTokenRepository.findByToken(token)).thenReturn(of(confirmationToken));
        confirmationTokenService.getConfirmationTokenByToken(token);

        // Then
        verify(confirmationTokenRepository).findByToken(token);
    }

    @Test
    void getConfirmationTokenByToken_TokenNotFound() {
        // Given
        // When
        when(confirmationTokenRepository.findByToken(token)).thenReturn(empty());

        // Then
        assertThatThrownBy(() -> confirmationTokenService.getConfirmationTokenByToken(token))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Token not found");
    }

    @Test
    void createEmailConfirmationToken_Success() {
        // Given
        User user = new User("userId", "Firstname", "Lastname", "firstname.lastname@email.com", "password", null);
        EmailConfirmationToken confirmationToken = new EmailConfirmationToken(token, user, null, false);

        // When
        confirmationTokenService.createEmailConfirmationToken(confirmationToken);

        // Then
        verify(confirmationTokenRepository).save(confirmationToken);
    }
}
