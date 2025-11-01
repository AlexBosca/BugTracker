package ro.alexportfolio.backend.unittests.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import ro.alexportfolio.backend.dao.UserRepository;
import ro.alexportfolio.backend.exception.UserNotFoundException;
import ro.alexportfolio.backend.model.EmailConfirmationToken;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.service.EmailConfirmationTokenService;
import ro.alexportfolio.backend.service.EmailSenderService;
import ro.alexportfolio.backend.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Clock clock;

    @Mock
    private EmailSenderService emailSenderService;
    
    @Mock
    private EmailConfirmationTokenService confirmationTokenService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private UserService userService;

    private static final ZonedDateTime NOW = ZonedDateTime.of(2022,
                                                       12,
                                                       26,
                                                       11,
                                                       30,
                                                       0,
                                                       0,
                                                       ZoneId.of("GMT"));

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder, clock, emailSenderService, confirmationTokenService);
    }

    @Test
    void createUser_ValidUser() {
        // Given
        User user = new User("userId", "Firstname", "Lastname", "firstname.lastname@email.com", "password", null);

        // When
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(clock.getZone()).thenReturn(NOW.getZone());

        userService.createUser(user);

        // Then
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getUserId()).isEqualTo("userId");
        assertThat(savedUser.getFirstName()).isEqualTo("Firstname");
        assertThat(savedUser.getLastName()).isEqualTo("Lastname");
        assertThat(savedUser.getEmail()).isEqualTo("firstname.lastname@email.com");
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getCreatedAt()).isEqualTo(NOW.toLocalDateTime());
        assertThat(savedUser.isEnabled()).isFalse();
    }

    @Test
    void createUser_EmailAlreadyInUse() {
        // Given
        User user = new User("userId", "Firstname", "Lastname", "firstname.lastname@email.com", "password", null);

        // When
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // Then
        assertThatThrownBy(() -> userService.createUser(user))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Email already in use");
    }

    @Test
    void confirmEmail_Success() {
        // Given
        String token = "validToken";
        User user = new User("userId", "Firstname", "Lastname", "firstname.lastname@email.com", "password", null);
        EmailConfirmationToken confirmationToken = new EmailConfirmationToken(token, user, NOW.toInstant().plusSeconds(3600), false);

        // When
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(confirmationTokenService.getConfirmationTokenByToken(token))
            .thenReturn(confirmationToken);
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(user));

        // Then
        String result = userService.confirmEmail(token);

        assertThat(result).isEqualTo("Account confirmed. You can log in.");
        assertThat(user.isEnabled()).isTrue();
        assertThat(confirmationToken.isUsed()).isTrue();
        verify(userRepository).save(user);
        verify(confirmationTokenService).createEmailConfirmationToken(confirmationToken);
    }

    @Test
    void confirmEmail_TokenExpired() {
        // Given
        String token = "expiredToken";
        User user = new User("userId", "Firstname", "Lastname", "firstname.lastname@email.com", "password", null);
        EmailConfirmationToken confirmationToken = new EmailConfirmationToken(token, user, NOW.toInstant().minusSeconds(3600), false);

        // When
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(confirmationTokenService.getConfirmationTokenByToken(token))
            .thenReturn(confirmationToken);

        // Then
        String result = userService.confirmEmail(token);
        assertThat(result).isEqualTo("Token expired or already used");
        assertThat(user.isEnabled()).isFalse();
        assertThat(confirmationToken.isUsed()).isFalse();
        verify(userRepository, never()).save(user);
        verify(confirmationTokenService, never()).createEmailConfirmationToken(confirmationToken);
    }

    @Test
    void confirmEmail_TokenAlreadyUsed() {
        // Given
        String token = "usedToken";
        User user = new User("userId", "Firstname", "Lastname", "firstname.lastname@email.com", "password", null);
        EmailConfirmationToken confirmationToken = new EmailConfirmationToken(token, user, NOW.toInstant().plusSeconds(3600), true);

        // When
        when(confirmationTokenService.getConfirmationTokenByToken(token))
            .thenReturn(confirmationToken);

        // Then
        String result = userService.confirmEmail(token);
        assertThat(result).isEqualTo("Token expired or already used");
        assertThat(user.isEnabled()).isFalse();
        assertThat(confirmationToken.isUsed()).isTrue();
        verify(userRepository, never()).save(user);
        verify(confirmationTokenService, never()).createEmailConfirmationToken(confirmationToken);
    }

    @Test
    void confirmEmail_UserNotFound() {
        // Given
        String token = "usedToken";
        User user = new User("userId", "Firstname", "Lastname", "firstname.lastname@email.com", "password", null);
        EmailConfirmationToken confirmationToken = new EmailConfirmationToken(token, user, NOW.toInstant().plusSeconds(3600), false);
        
        // When
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(confirmationTokenService.getConfirmationTokenByToken(token))
            .thenReturn(confirmationToken);
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> userService.confirmEmail(token))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("No user found");
        assertThat(user.isEnabled()).isFalse();
        assertThat(confirmationToken.isUsed()).isFalse();
        verify(userRepository, never()).save(user);
        verify(confirmationTokenService, never()).createEmailConfirmationToken(confirmationToken);
    }

    @Test
    void getUserByUserId_UserExists() {
        // Given
        String userId = "userId";
        User user = new User("userId", "Firstname", "Lastname", "firstname.lastname@email.com", "password", null);
        
        // When
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByUserId(userId);

        // Then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void getUserByUserId_UserDoesNotExist() {
        // Given
        String userId = "nonExistingUserId";

        // When
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> userService.getUserByUserId(userId))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("User not found");
    }

    @Test
    void getAllUsers() {
        // Given
        User user = new User("userId", "Firstname", "Lastname", "firstname.lastname@email.com", "password", null);

        // When
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        // Then
        assertThat(users).hasSize(1);
        assertThat(users.get(0)).isEqualTo(user);
    }

    @Test
    void getUserByEmail_UserExists() {
        // Given
        String email = "firstname.lastname@email.com";
        User user = new User("userId", "Firstname", "Lastname", email, "password", null);

        // When
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEamil(email);

        // Then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void getUserByEmail_UserDoesNotExist() {
        // Given
        String email = "firstname.lastname@email.com";

        // When
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> userService.getUserByEamil(email))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("User not found");
    }

    @Test
    void updateUser_UserExists() {
        // Given
        String userId = "userId";
        User user = new User("userId", "Firstname", "Lastname", "firstname.lastname@email.com", "password", null);
        User updatedInfo = new User("userId", "Firstname", "Lastname", "firstname.lastname@o-email.com", "newpassword", null);

        // When
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(updatedInfo.getPassword())).thenReturn("encodedNewPassword");

        userService.updateUser(userId, updatedInfo);

        // Then
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("firstname.lastname@o-email.com");
        assertThat(savedUser.getPassword()).isEqualTo("encodedNewPassword");
        assertThat(savedUser.getUserId()).isEqualTo("userId");
    }

    @Test
    void updateUser_UserDoesNotExist() {
        // Given
        String userId = "nonExistingUserId";
        User updatedInfo = new User("userId", "Firstname", "Lastname", "firstname.lastname@o-email.com", "newpassword", null);

        // When
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());
        
        // Then
        assertThatThrownBy(() -> userService.updateUser(userId, updatedInfo))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("User not found");

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_UserExists() {
        // Given
        String userId = "userId";

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository).deleteByUserId(userId);
    }
}
