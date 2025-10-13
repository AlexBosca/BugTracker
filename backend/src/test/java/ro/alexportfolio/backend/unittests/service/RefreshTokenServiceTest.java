package ro.alexportfolio.backend.unittests.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.alexportfolio.backend.dao.RefreshTokensRepository;
import ro.alexportfolio.backend.model.GlobalRole;
import ro.alexportfolio.backend.model.RefreshToken;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.service.RefreshTokenService;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    
    @Mock
    private RefreshTokensRepository refreshTokensRepository;

    // @Captor
    // private ArgumentCaptor

    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        refreshTokenService = new RefreshTokenService(refreshTokensRepository);
    }

    @Test
    void getRefreshToken_Success() {
        // Given
        String token = "sampleToken";
        User user = new User("JoDo34",
                             "John",
                             "Doe",
                             "john.doe@mail.com",
                             "userP4ssw@rd",
                             GlobalRole.USER);
        RefreshToken mockRefreshToken = new RefreshToken(token,
                                                         false,
                                                         user);

        // When
        when(refreshTokensRepository.findByToken(token))
            .thenReturn(Optional.of(mockRefreshToken));

        refreshTokenService.getRefreshToken(token);

        // Then
        verify(refreshTokensRepository, times(1)).findByToken(token);
        assertThat(mockRefreshToken.getToken()).isEqualTo(token);
        assertThat(mockRefreshToken.getUser()).isEqualTo(user);
        assertThat(mockRefreshToken.isRevoked()).isFalse();
    }

    @Test
    void getRefreshToken_Failure() {
        // Given
        String token = "sampleToken";

        // When
        when(refreshTokensRepository.findByToken(token))
            .thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> refreshTokenService.getRefreshToken(token))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("No token found");
    }

    @Test
    void createRefreshToken_Success() {
        // Given
        String token = "sampleToken";
        User user = new User("JoDo34",
                             "John",
                             "Doe",
                             "john.doe@mail.com",
                             "userP4ssw@rd",
                             GlobalRole.USER);

        // When
        refreshTokenService.createRefreshToken(token, false, user);

        // Then
        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokensRepository, times(1)).save(captor.capture());
        RefreshToken savedToken = captor.getValue();
        assertThat(savedToken.getToken()).isEqualTo(token);
        assertThat(savedToken.getUser()).isEqualTo(user);
        assertThat(savedToken.isRevoked()).isFalse();
    }

    @Test
    void deleteRefreshToken_Success() {
        // Given
        String token = "sampleToken";
        User user = new User("JoDo34",
                             "John",
                             "Doe",
                             "john.doe@mail.com",
                             "userP4ssw@rd",
                             GlobalRole.USER);
        RefreshToken mockRefreshToken = new RefreshToken(token,
                                                         false,
                                                         user);

        // When
        when(refreshTokensRepository.findByToken(token))
            .thenReturn(Optional.of(mockRefreshToken));
        
        refreshTokenService.deleteRefreshToken(token);

        // Then
        verify(refreshTokensRepository, times(1)).findByToken(token);
        verify(refreshTokensRepository, times(1)).delete(mockRefreshToken);
    }

    @Test
    void deleteRefreshToken_Failure() {
        // Given
        String token = "sampleToken";

        // When
        when(refreshTokensRepository.findByToken(token))
            .thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> refreshTokenService.deleteRefreshToken(token))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("No token found");
    }
}
