package com.example.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dao.UserDao;
import com.example.backend.entity.UserEntity;

import static com.example.backend.util.ExceptionUtilities.*;

@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AppUserDetailsServiceTest {

    @Mock
    private UserDao userDao;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private Clock clock;

    @Captor
    private ArgumentCaptor<UserEntity> userArgumentCaptor;

    private AppUserDetailsService appUserDetailsService;

    @BeforeEach
    void setUp() {
        appUserDetailsService = new AppUserDetailsService(
            userDao,
            passwordEncoder,
            confirmationTokenService,
            clock
        );
    }

    @Test
    @DisplayName("Should load user by email")
    public void shouldLoadUserByEmail() {
        UserEntity expectedUser = UserEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .build();

        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.of(expectedUser));

        assertThat(appUserDetailsService.loadUserByUsername("john.doe@gmail.com")).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("Should throw an exception when try to load user by email that doesn't exist")
    public void shouldThrowExceptionWhenLoadUserByEmailWhenUserDoesNotExist() {
        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            appUserDetailsService.loadUserByUsername("john.doe@gmail.com");
        }).isInstanceOf(UsernameNotFoundException.class)
        .hasMessage(String.format(USER_WITH_EMAIL_NOT_FOUND, "john.doe@gmail.com"));
    }

    @Disabled
    @Test
    void testSignUpUser() {
        
    }
}
