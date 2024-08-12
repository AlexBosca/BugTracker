package com.example.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dao.UserDao;
import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.UserRequest;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.user.UserCredentialsNotValidException;
import com.example.backend.exception.user.UserEmailNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;

import static com.example.backend.util.ExceptionUtilities.*;
import static java.time.ZonedDateTime.of;

@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {

    @Mock
    private UserDao userDao;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private Clock clock;

    @Mock
    private AvatarService avatarService;

    private static ZonedDateTime NOW = of(2022,
                                            12,
                                            26, 
                                            11, 
                                            30, 
                                            0, 
                                            0, 
                                            ZoneId.of("GMT"));

    @Captor
    private ArgumentCaptor<UserEntity> userArgumentCaptor;

    private AppUserDetailsService appUserDetailsService;

    @BeforeEach
    void setUp() {
        appUserDetailsService = spy(new AppUserDetailsService(
            userDao,
            passwordEncoder,
            confirmationTokenService,
            clock,
            avatarService
        ));
    }

    @Test
    @DisplayName("Should load user by email")
    void loadUserByUsername_NoExceptionThrown() {
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
    void loadUserByUsername_UserCredentialsNotValidExceptionThrown() {
        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            appUserDetailsService.loadUserByUsername("john.doe@gmail.com");
        }).isInstanceOf(UserCredentialsNotValidException.class)
        .hasMessage(String.format(USER_CREDENTIALS_NOT_VALID, "john.doe@gmail.com"));
    }

    @Test
    @DisplayName("Should load user by userId")
    void loadUserByUserId_NoExceptionThrown() {
        UserEntity expectedUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .build();

        when(userDao.selectUserByUserId("JD00001")).thenReturn(Optional.of(expectedUser));

        assertThat(appUserDetailsService.loadUserByUserId("JD00001")).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("Should throw an exception when try to load user by userId that doesn't exist")
    void loadUserByUserId_UserIdNotFoundExceptionThrown() {
        when(userDao.selectUserByUserId("JD00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            appUserDetailsService.loadUserByUserId("JD00001");
        }).isInstanceOf(UserIdNotFoundException.class)
        .hasMessage(String.format(USER_WITH_ID_NOT_FOUND, "JD00001"));
    }

    @Test
    @DisplayName("Should return a not empty list when there are users")
    void loadAllUsers_ExistingUsers() {
        UserEntity firstExpectedUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .build();

        UserEntity secondExpectedUser = UserEntity.builder()
            .userId("TJ00002")
            .firstName("Travis")
            .lastName("Johnson")
            .email("tavis.johnson@yahoo.com")
            .build();

        List<UserEntity> expectedUsers = List.of(
            firstExpectedUser,
            secondExpectedUser
        );

        when(userDao.selectAllUsers()).thenReturn(List.of(
            firstExpectedUser,
            secondExpectedUser
        ));

        assertThat(appUserDetailsService.loadAllUsers()).isNotEmpty();
        assertThat(appUserDetailsService.loadAllUsers()).isEqualTo(expectedUsers);
    }

    @Test
    @DisplayName("Should return an empty list when there are no users")
    void loadAllUsers_NoUsers() {
        when(userDao.selectAllUsers()).thenReturn(List.of());

        assertThat(appUserDetailsService.loadAllUsers()).isEmpty();
    }

    @Test
    @DisplayName("Should return a not empty list when there are users to filter")
    void filterUsers_ExistingUsers() {
        UserEntity firstExpectedUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .build();

        UserEntity secondExpectedUser = UserEntity.builder()
            .userId("TD00002")
            .firstName("Travis")
            .lastName("Doe")
            .email("tavis.doe@yahoo.com")
            .build();

        Map<String, Object> filters = new HashMap<>();
        filters.put("lastName", "Doe");

        Map<String, String> operators = new HashMap<>();
        operators.put("lastName", "=");

        Map<String, String> dataTypes = new HashMap<>();
        dataTypes.put("lastName", "string");

        FilterCriteria filterCriteria = new FilterCriteria(
            filters,
            operators,
            dataTypes
        );

        List<UserEntity> expectedUsers = List.of(
            firstExpectedUser,
            secondExpectedUser
        );

        when(userDao.selectAllFilteredUsers(filterCriteria)).thenReturn(List.of(firstExpectedUser, secondExpectedUser));

        assertThat(appUserDetailsService.filterUsers(filterCriteria)).isNotEmpty();
        assertThat(appUserDetailsService.filterUsers(filterCriteria)).isEqualTo(expectedUsers);
    }

    @Test
    @DisplayName("Should return an empty list when there are no users to filter")
    void filterUsers_NoUsers() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("lastName", "Doe");

        Map<String, String> operators = new HashMap<>();
        operators.put("lastName", "=");

        Map<String, String> dataTypes = new HashMap<>();
        dataTypes.put("lastName", "string");

        FilterCriteria filterCriteria = new FilterCriteria(
            filters,
            operators,
            dataTypes
        );
        
        when(userDao.selectAllFilteredUsers(filterCriteria)).thenReturn(List.of());

        assertThat(appUserDetailsService.filterUsers(filterCriteria)).isEmpty();
    }

    @Test
    @DisplayName("Should save user that doesn't exist")
    void saveUser_NoExceptionThrown() {
        UserEntity expectedUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .build();

        
        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        appUserDetailsService.saveUser(expectedUser);

        verify(appUserDetailsService, times(1)).setPasswordToUser(expectedUser);
        verify(appUserDetailsService, times(1)).setupAccount(expectedUser.getUserId());
    }

    // @Disabled
    // @Test
    // @DisplayName("Should throw an exception when try to save an user that already exist")
    // void saveUser_UserIdNotFoundExceptionThrown() {
    //     UserEntity expectedUser = UserEntity.builder()
    //         .userId("JD00001")
    //         .firstName("John")
    //         .lastName("Doe")
    //         .email("john.doe@gmail.com")
    //         .build();

        
    //     when(clock.getZone()).thenReturn(NOW.getZone());
    //     when(clock.instant()).thenReturn(NOW.toInstant());

    //     appUserDetailsService.saveUser(expectedUser);

    //     verify(appUserDetailsService, times(1)).setPasswordToUser(expectedUser);
    //     verify(appUserDetailsService, times(1)).setupAccount(expectedUser.getUserId());
    // }

    @Test
    @DisplayName("Should update user details and avatar when user exists")
    void updateUser_ExistingUserAllFields() {
        UserEntity expectedUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("newPassword")
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("newPassword")
            .build();

        MockMultipartFile avatar = new MockMultipartFile(
            "file", "avatar.jpg", "image/jpeg", "avatar content".getBytes());

        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.of(expectedUser));
        doNothing().when(appUserDetailsService).uploadAvatar("JD00001", avatar);
        when(passwordEncoder.encode(expectedRequest.getPassword())).thenReturn("encodedPassword");

        appUserDetailsService.updateUser("john.doe@gmail.com", avatar, expectedRequest);
        
        verify(appUserDetailsService, times(1)).uploadAvatar("JD00001", avatar);
        verify(passwordEncoder, times(1)).encode("newPassword");
        assertThat(expectedRequest.getPassword()).isEqualTo("encodedPassword");
        verify(userDao, times(1)).updateUser("john.doe@gmail.com", expectedRequest);
    }

    @Test
    @DisplayName("Should update user details when user exists and avatar is null")
    void updateUser_NullAvatar() {
        UserEntity expectedUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("newPassword")
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("newPassword")
            .build();

        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.of(expectedUser));
        when(passwordEncoder.encode(expectedRequest.getPassword())).thenReturn("encodedPassword");

        appUserDetailsService.updateUser("john.doe@gmail.com", null, expectedRequest);
        
        verify(passwordEncoder, times(1)).encode("newPassword");
        assertThat(expectedRequest.getPassword()).isEqualTo("encodedPassword");
        verify(userDao, times(1)).updateUser("john.doe@gmail.com", expectedRequest);
    }

    @Test
    @DisplayName("Should update user details when user exists and avatar is empty")
    void updateUser_EmptyAvatar() {
        UserEntity expectedUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("newPassword")
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("newPassword")
            .build();

        MockMultipartFile avatar = new MockMultipartFile(
            "file", "avatar.jpg", "image/jpeg", new byte[0]);

        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.of(expectedUser));
        when(passwordEncoder.encode(expectedRequest.getPassword())).thenReturn("encodedPassword");

        appUserDetailsService.updateUser("john.doe@gmail.com", avatar, expectedRequest);
        
        verify(passwordEncoder, times(1)).encode("newPassword");
        assertThat(expectedRequest.getPassword()).isEqualTo("encodedPassword");
        verify(userDao, times(1)).updateUser("john.doe@gmail.com", expectedRequest);
    }

    @Test
    @DisplayName("Should update user details and avatar when user exists and password is null")
    void updateUser_NullPassword() {
        UserEntity expectedUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .build();

        MockMultipartFile avatar = new MockMultipartFile(
            "file", "avatar.jpg", "image/jpeg", "avatar content".getBytes());

        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.of(expectedUser));
        doNothing().when(appUserDetailsService).uploadAvatar("JD00001", avatar);

        appUserDetailsService.updateUser("john.doe@gmail.com", avatar, expectedRequest);
        
        verify(appUserDetailsService, times(1)).uploadAvatar("JD00001", avatar);
        verify(userDao, times(1)).updateUser("john.doe@gmail.com", expectedRequest);
    }

    @Test
    @DisplayName("Should update user details and avatar when user exists and password is empty")
    void updateUser_EmptyPassword() {
        UserEntity expectedUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("")
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("")
            .build();

        MockMultipartFile avatar = new MockMultipartFile(
            "file", "avatar.jpg", "image/jpeg", "avatar content".getBytes());

        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.of(expectedUser));
        doNothing().when(appUserDetailsService).uploadAvatar("JD00001", avatar);

        appUserDetailsService.updateUser("john.doe@gmail.com", avatar, expectedRequest);
        
        verify(appUserDetailsService, times(1)).uploadAvatar("JD00001", avatar);
        verify(userDao, times(1)).updateUser("john.doe@gmail.com", expectedRequest);
    }

    @Test
    @DisplayName("Should throw an exception when trying to update details for a non existing user")
    void updateUser_UserEmailNotFoundExceptionThrown() {
        UserEntity expectedUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("newPassword")
            .build();

        UserRequest expectedRequest = UserRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .password("newPassword")
            .build();

        MockMultipartFile avatar = new MockMultipartFile(
            "file", "avatar.jpg", "image/jpeg", new byte[0]);

        when(userDao.selectUserByEmail("john.doe@gmail.com")).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> {
            appUserDetailsService.updateUser("john.doe@gmail.com", avatar, expectedRequest);
        }).isInstanceOf(UserEmailNotFoundException.class)
        .hasMessage(String.format(USER_WITH_EMAIL_NOT_FOUND, "john.doe@gmail.com"));
    }

    @Test
    @DisplayName("Should upload avatar of an existing user")
    void uploadAvatar_NoExceptionThrown() {
        UserEntity expectedUser = UserEntity.builder()
            .userId("JD00001")
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@gmail.com")
            .build();

        MockMultipartFile avatar = new MockMultipartFile(
                "file", "avatar.jpg", "image/jpeg", "avatar content".getBytes());

        when(userDao.selectUserByUserId("JD00001")).thenReturn(Optional.of(expectedUser));
        when(avatarService.saveAvatar(avatar, "JD00001")).thenReturn("JD00001_avatar");
        
        appUserDetailsService.uploadAvatar("JD00001", avatar);

        verify(userDao, times(1)).selectUserByUserId("JD00001");
        verify(avatarService, times(1)).saveAvatar(avatar, "JD00001");
        verify(userDao, times(1)).insertUser(expectedUser);

        assertThat(expectedUser.getAvatarUrl()).isEqualTo("JD00001_avatar");
    }

    @Test
    @DisplayName("Should throw an exception when trying to upload avatar for a non existing user")
    void uploadAvatar_UserIdNotFoundExceptionThrown() {
        MockMultipartFile avatar = new MockMultipartFile(
                "file", "avatar.jpg", "image/jpeg", "avatar content".getBytes());

        when(userDao.selectUserByUserId("JD00001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            appUserDetailsService.uploadAvatar("JD00001", avatar);
        }).isInstanceOf(UserIdNotFoundException.class)
        .hasMessage(String.format(USER_WITH_ID_NOT_FOUND, "JD00001"));
    }
}
