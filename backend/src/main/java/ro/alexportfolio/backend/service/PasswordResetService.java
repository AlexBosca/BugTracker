package ro.alexportfolio.backend.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ro.alexportfolio.backend.dao.PasswordResetTokenRepository;
import ro.alexportfolio.backend.dao.UserRepository;
import ro.alexportfolio.backend.exception.TokenNotFoundOrExpiredException;
import ro.alexportfolio.backend.exception.UserNotFoundException;
import ro.alexportfolio.backend.model.EmailData;
import ro.alexportfolio.backend.model.PasswordResetToken;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.util.EmailConstants;
import ro.alexportfolio.backend.util.TokenGenerator;

@Service
public class PasswordResetService {
    
    private final EmailSenderService emailSenderService;
    private final UserRepository userRepository;
    private final Clock clock;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.name}")
    private String applicationName;

    public PasswordResetService(final @Qualifier("passwordReset") EmailSenderService emailSenderServiceParam,
                                final UserRepository userRepositoryParamParam,
                                final Clock clockParamParam,
                                final PasswordResetTokenRepository passwordResetTokenRepositoryParam,
                                final PasswordEncoder passwordEncoderParam) {
        this.emailSenderService = emailSenderServiceParam;
        this.userRepository = userRepositoryParamParam;
        this.clock = clockParamParam;
        this.passwordResetTokenRepository = passwordResetTokenRepositoryParam;
        this.passwordEncoder = passwordEncoderParam;
    }

    public void initiatePasswordReset(final String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()) {
            return;
        }

        User user = userOptional.get();
        String token = TokenGenerator.generateToken();

        PasswordResetToken passwordResetToken = new PasswordResetToken(
            token,
            user,
            Instant.now(clock).plus(Duration.ofHours(2))
        );

        passwordResetTokenRepository.save(passwordResetToken);

        EmailData emailData = EmailData.builder()
            .recipientName(user.getFullName())
            .recipientEmail(user.getEmail())
            .subject(EmailConstants.EMAIL_PASSWORD_RESET_SUBJECT.getValue())
            .title(EmailConstants.EMAIL_PASSWORD_RESET_TITLE.getValue())
            .applicationName(applicationName)
            .confirmationLink(Optional.of(EmailConstants.EMAIL_PASSWORD_RESET_LINK.getValue(token)))
            .notificationContent(Optional.empty())
            .build();

        emailSenderService.sendEmail(emailData);
    }

    public void validatePasswordResetToken(final String token) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);
        boolean isTokenExpired = tokenOptional
            .map(t -> t.getExpiresAt().isBefore(Instant.now(clock)))
            .orElse(true);

        if(tokenOptional.isEmpty() || isTokenExpired) {
            throw new TokenNotFoundOrExpiredException();
        }
    }

    public void resetPassword(final String token, final String newPassword) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);

        if(tokenOptional.isEmpty() || tokenOptional.get().getExpiresAt().isBefore(Instant.now(clock))) {
            throw new TokenNotFoundOrExpiredException();
        }

        PasswordResetToken passwordResetToken = tokenOptional.get();
        User user = userRepository.findByUserId(passwordResetToken.getUserId())
            .orElseThrow(UserNotFoundException::new);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(passwordResetToken);

        EmailData emailData = EmailData.builder()
            .recipientName(user.getFullName())
            .recipientEmail(user.getEmail())
            .subject(EmailConstants.EMAIL_PASSWORD_RESET_CONFIRMATION_SUBJECT.getValue())
            .title(EmailConstants.EMAIL_PASSWORD_RESET_CONFIRMATION_TITLE.getValue())
            .applicationName(applicationName)
            .confirmationLink(Optional.empty())
            .notificationContent(Optional.of(EmailConstants.EMAIL_PASSWORD_RESET_CONFIRMATION_CONTENT.getValue()))
            .build();

        emailSenderService.sendEmail(emailData);
    }
}
