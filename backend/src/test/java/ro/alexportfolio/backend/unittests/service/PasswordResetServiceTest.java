package ro.alexportfolio.backend.unittests.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import ro.alexportfolio.backend.dao.PasswordResetTokenRepository;
import ro.alexportfolio.backend.dao.UserRepository;
import ro.alexportfolio.backend.exception.TokenNotFoundOrExpiredException;
import ro.alexportfolio.backend.model.EmailData;
import ro.alexportfolio.backend.model.GlobalRole;
import ro.alexportfolio.backend.model.PasswordResetToken;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.service.EmailSenderService;
import ro.alexportfolio.backend.service.PasswordResetService;
import ro.alexportfolio.backend.util.EmailConstants;
import ro.alexportfolio.backend.util.TokenGenerator;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private Clock clock;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<PasswordResetToken> passwordResetTokenCaptor;

    @Captor
    private ArgumentCaptor<EmailData> emailDataCaptor;
    
    private PasswordResetService passwordResetService;

    private static final ZonedDateTime NOW = ZonedDateTime.of(2022,
                                                       12,
                                                       26,
                                                       11,
                                                       30,
                                                       0,
                                                       0,
                                                       ZoneId.of("GMT"));

    @BeforeEach
    void setup() {
        this.passwordResetService = new PasswordResetService(emailSenderService,
                                                              userRepository,
                                                              clock,
                                                              passwordResetTokenRepository,
                                                              passwordEncoder);
    }

    @Test
    void initiatePasswordReset_UserExists() {
        try (MockedStatic<TokenGenerator> mockedTokenGenerator = mockStatic(TokenGenerator.class)) {
            // given
            User user = new User("JoDo34",
                                "John",
                                "Doe",
                                "john.doe@mail.com",
                                "userP4ssw@rd",
                                GlobalRole.USER);

            ReflectionTestUtils.setField(passwordResetService, "applicationName", "Issue Tracker");

            // when
            when(clock.instant()).thenReturn(NOW.toInstant());
            when(userRepository.findByEmail("john.doe@mail.com")).thenReturn(Optional.of(user));
            mockedTokenGenerator.when(TokenGenerator::generateToken).thenReturn("randomTokenString");
            
            passwordResetService.initiatePasswordReset("john.doe@mail.com");

            // then
            verify(passwordResetTokenRepository, times(1)).save(passwordResetTokenCaptor.capture());
            verify(emailSenderService, times(1)).sendEmail(emailDataCaptor.capture());
            mockedTokenGenerator.verify(TokenGenerator::generateToken, times(1));

            PasswordResetToken capturedToken = passwordResetTokenCaptor.getValue();
            assertThat(capturedToken.getToken()).isEqualTo("randomTokenString"); //TODO: Check how to mock static method
            assertThat(capturedToken.getUser()).isEqualTo(user);
            assertThat(capturedToken.getExpiresAt()).isEqualTo(NOW.toInstant().plus(Duration.ofHours(2)));

            EmailData capturedEmailData = emailDataCaptor.getValue();
            assertThat(capturedEmailData.recipientName()).isEqualTo("John Doe");
            assertThat(capturedEmailData.recipientEmail()).isEqualTo("john.doe@mail.com");
            assertThat(capturedEmailData.subject()).isEqualTo(EmailConstants.EMAIL_PASSWORD_RESET_SUBJECT.getValue());
            assertThat(capturedEmailData.title()).isEqualTo(EmailConstants.EMAIL_PASSWORD_RESET_TITLE.getValue());
            assertThat(capturedEmailData.applicationName()).isEqualTo("Issue Tracker");
            assertThat(capturedEmailData.confirmationLink()).isPresent();
            assertThat(capturedEmailData.notificationContent()).isEmpty();
        }
    }

    @Test
    void initiatePasswordReset_UserDoesNotExist() {
        // given
        String email = "john.doe@mail.org";

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        passwordResetService.initiatePasswordReset(email);

        // then
        verify(passwordResetTokenRepository, times(0)).save(passwordResetTokenCaptor.capture());
        verify(emailSenderService, times(0)).sendEmail(emailDataCaptor.capture());
    }

    @Test
    void validatePasswordResetToken_TokenExists() {
        // given
        String token = "randomTokenString";
        User user = new User("JoDo34",
                             "John",
                             "Doe",
                             "john.doe@mail.com",
                             "userP4ssw@rd",
                             GlobalRole.USER);

        PasswordResetToken passwordResetToken = new PasswordResetToken(
            token,
            user,
            NOW.toInstant().plus(Duration.ofHours(2))
        );

        // when
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(passwordResetTokenRepository.findByToken(token))
            .thenReturn(Optional.of(passwordResetToken));

        passwordResetService.validatePasswordResetToken(token);

        // then
        verify(passwordResetTokenRepository, times(1)).findByToken(token);
    }

    @Test
    void validatePasswordResetToken_TokenDoesNotExist() {
        // given
        String token = "randomTokenString";

        // when
        when(passwordResetTokenRepository.findByToken(token))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> passwordResetService.validatePasswordResetToken(token))
            .isInstanceOf(TokenNotFoundOrExpiredException.class);
    }

    @Test
    void validatePasswordResetToken_TokenExpired() {
        // given
        String token = "randomTokenString";
        User user = new User("JoDo34",
                             "John",
                             "Doe",
                             "john.doe@mail.com",
                             "userP4ssw@rd",
                             GlobalRole.USER);

        PasswordResetToken passwordResetToken = new PasswordResetToken(
            token,
            user,
            NOW.toInstant().minus(Duration.ofHours(2))
        );

        // when
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(passwordResetTokenRepository.findByToken(token))
            .thenReturn(Optional.of(passwordResetToken));

        // then
        assertThatThrownBy(() -> passwordResetService.validatePasswordResetToken(token))
            .isInstanceOf(TokenNotFoundOrExpiredException.class);
    }

    @Test
    void resetPassword_TokenExists() {
        // given
        String token = "randomTokenString";
        User user = new User("JoDo34",
                             "John",
                             "Doe",
                             "john.doe@mail.com",
                             "userP4ssw@rd",
                             GlobalRole.USER);

        PasswordResetToken passwordResetToken = new PasswordResetToken(
            token,
            user,
            NOW.toInstant().plus(Duration.ofHours(2))
        );
        
        // when
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(passwordResetTokenRepository.findByToken(token))
            .thenReturn(Optional.of(passwordResetToken));
        when(userRepository.findByUserId(user.getUserId()))
            .thenReturn(Optional.of(user));
        when(passwordEncoder.encode("N3wP4ssw@rd")).thenReturn("EncodedN3wP4ssw@rd");

        ReflectionTestUtils.setField(passwordResetService, "applicationName", "Issue Tracker");
        
        passwordResetService.resetPassword(token, "N3wP4ssw@rd");

        // then
        verify(passwordResetTokenRepository, times(1)).findByToken(token);
        verify(userRepository, times(1)).findByUserId(user.getUserId());
        verify(userRepository, times(1)).save(user);
        verify(passwordResetTokenRepository, times(1)).delete(passwordResetToken);
        verify(emailSenderService, times(1)).sendEmail(emailDataCaptor.capture());

        assertThat(user.getPassword()).isEqualTo("EncodedN3wP4ssw@rd");

        EmailData capturedEmailData = emailDataCaptor.getValue();
        assertThat(capturedEmailData.recipientName()).isEqualTo("John Doe");
        assertThat(capturedEmailData.recipientEmail()).isEqualTo("john.doe@mail.com");
        assertThat(capturedEmailData.subject()).isEqualTo(EmailConstants.EMAIL_PASSWORD_RESET_CONFIRMATION_SUBJECT.getValue());
        assertThat(capturedEmailData.title()).isEqualTo(EmailConstants.EMAIL_PASSWORD_RESET_CONFIRMATION_TITLE.getValue());
        assertThat(capturedEmailData.applicationName()).isEqualTo("Issue Tracker");
        assertThat(capturedEmailData.confirmationLink()).isEmpty();
        assertThat(capturedEmailData.notificationContent())
            .isEqualTo(Optional.of(EmailConstants.EMAIL_PASSWORD_RESET_CONFIRMATION_CONTENT.getValue()));
    }

    @Test
    void resetPassword_TokenDoesNotExist() {
        // given
        String token = "randomTokenString";
        String newPassword = "N3wP4ssw@rd";

        // when
        when(passwordResetTokenRepository.findByToken(token))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> passwordResetService.resetPassword(token, newPassword))
            .isInstanceOf(TokenNotFoundOrExpiredException.class);
    }

    @Test
    void resetPassword_TokenExpired() {
        // given
        String token = "randomTokenString";
        String newPassword = "N3wP4ssw@rd";
        User user = new User("JoDo34",
                             "John",
                             "Doe",
                             "john.doe@mail.com",
                             "userP4ssw@rd",
                             GlobalRole.USER);

        PasswordResetToken passwordResetToken = new PasswordResetToken(
            token,
            user,
            NOW.toInstant().minus(Duration.ofHours(2))
        );

        // when
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(passwordResetTokenRepository.findByToken(token))
            .thenReturn(Optional.of(passwordResetToken));

        // then
        assertThatThrownBy(() -> passwordResetService.resetPassword(token, newPassword))
            .isInstanceOf(TokenNotFoundOrExpiredException.class);
    }

    @Test
    void resetPassword_UserNotFound() {
        // given
        String token = "randomTokenString";
        String newPassword = "N3wP4ssw@rd";
        User user = new User("JoDo34",
                             "John",
                             "Doe",
                             "john.doe@mail.com",
                             "userP4ssw@rd",
                             GlobalRole.USER);

        PasswordResetToken passwordResetToken = new PasswordResetToken(
            token,
            user,
            NOW.toInstant().plus(Duration.ofHours(2))
        );

        // when
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(passwordResetTokenRepository.findByToken(token))
            .thenReturn(Optional.of(passwordResetToken));
        when(userRepository.findByUserId(user.getUserId()))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> passwordResetService.resetPassword(token, newPassword))
            .isInstanceOf(ro.alexportfolio.backend.exception.UserNotFoundException.class);
    }
}
