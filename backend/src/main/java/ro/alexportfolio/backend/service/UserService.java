package ro.alexportfolio.backend.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.alexportfolio.backend.dao.UserRepository;
import ro.alexportfolio.backend.exception.UserNotFoundException;
import ro.alexportfolio.backend.model.EmailConfirmationToken;
import ro.alexportfolio.backend.model.EmailData;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.util.EmailConstants;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private static final int CONFIRMATION_TOKEN_EXPIRATION_HOURS = 24;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private final EmailSenderService emailSenderService;
    private final EmailConfirmationTokenService confirmationTokenService;

    @Value("${application.name}")
    private String applicationName;

    public UserService(final UserRepository userRepositoryParam,
                       final PasswordEncoder passwordEncoderParam,
                       final Clock clockParam,
                       final @Qualifier("registration") EmailSenderService emailSenderServiceParam,
                       final EmailConfirmationTokenService tokenServiceParam) {
        this.userRepository = userRepositoryParam;
        this.passwordEncoder = passwordEncoderParam;
        this.clock = clockParam;
        this.emailSenderService = emailSenderServiceParam;
        this.confirmationTokenService = tokenServiceParam;
    }

    public void createUser(final User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setCreatedAt(LocalDateTime.now(clock));

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        EmailConfirmationToken confirmationToken = new EmailConfirmationToken(
            token,
            user,
            Instant.now(clock).plus(Duration.ofHours(CONFIRMATION_TOKEN_EXPIRATION_HOURS)),
            false
        );

        confirmationTokenService.createEmailConfirmationToken(confirmationToken);

        EmailData emailData = EmailData.builder()
            .recipientName(user.getFullName())
            .recipientEmail(user.getEmail())
            .subject(EmailConstants.EMAIL_ACCOUNT_CONFIRMATION_SUBJECT.getValue())
            .title(EmailConstants.EMAIL_ACCOUNT_CONFIRMATION_TITLE.getValue())
            .applicationName(applicationName)
            .confirmationLink(Optional.of(EmailConstants.EMAIL_ACCOUNT_CONFIRMATION_LINK.getValue(confirmationToken.getToken())))
            .notificationContent(Optional.empty())
            .build();

        emailSenderService.sendEmail(emailData);
    }

    public String confirmEmail(String token) {
        EmailConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenByToken(token);

        if(confirmationToken.isUsed() || confirmationToken.getExpiresAt().isBefore(Instant.now(clock))) {
            return "Token expired or already used";
        }

        User user = userRepository.findByUserId(confirmationToken.getUserId()).orElseThrow(() -> new RuntimeException("No user found"));
        user.enableAccount();
        confirmationToken.setUsed(true);

        userRepository.save(user);
        // confirmationTokenService.update(confirmationToken);
        confirmationTokenService.createEmailConfirmationToken(confirmationToken);

        return "Account confirmed. You can log in.";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUserId(final String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public User getUserByEamil(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    public void updateUser(final String userId,
                           final User user) {
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setGlobalRole(user.getGlobalRole());

        userRepository.save(existingUser);
    }

    public void deleteUser(final String userId) {
        userRepository.deleteByUserId(userId);
    }
}
