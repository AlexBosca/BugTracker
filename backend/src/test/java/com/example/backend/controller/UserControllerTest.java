package com.example.backend.controller;

import static com.example.backend.util.ExceptionUtilities.USER_CREDENTIALS_NOT_VALID;
import static com.example.backend.util.ExceptionUtilities.USER_WITH_ID_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.backend.entity.UserEntity;
import com.example.backend.exception.issue.IssueNotFoundException;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.exception.user.UserCredentialsNotValidException;
import com.example.backend.exception.user.UserIdNotFoundException;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.AppUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Profile("test")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class, useDefaultFilters = false, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(UserController.class)
@ComponentScan({"com.example.backend.mapper"})
@WithMockUser(username = "test.user@domain.com")
class UserControllerTest {

    @Autowired
    private MapStructMapper mapStructMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppUserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Clock clock;

    @Test
    @DisplayName("Should return OK Response when no exception was thrown when calling the upload-avatar endpoint")
    void uploadAvatar_NoExceptionThrown_OkResponse() throws Exception {
        UserEntity user = UserEntity.builder()
            .userId("TU00001")
            .firstName("Test")
            .lastName("User")
            .email("test.user@domain.com")
            .build();

        MockMultipartFile avatar = new MockMultipartFile(
                "avatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());

        when(userDetailsService.loadUserByUsername("test.user@domain.com")).thenReturn(user);

        mockMvc.perform(multipart("/users/upload-avatar")
                .file(avatar))
            .andExpect(status().isOk());

        verify(userDetailsService).loadUserByUsername("test.user@domain.com");
        verify(userDetailsService).uploadAvatar("TU00001", avatar);
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED Response and resolve exception when UserCredentialsNotValidException was thrown when calling the upload-avatar endpoint")
    void uploadAvatar_UserCredentialsNotValidExceptionThrown_ResolveExceptionAndUnauthorizedResponse() throws Exception {
        MockMultipartFile avatar = new MockMultipartFile(
                "avatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());

        when(userDetailsService.loadUserByUsername("test.user@domain.com")).thenThrow(new UserCredentialsNotValidException());

        mockMvc.perform(multipart("/users/upload-avatar")
                .file(avatar))
            .andExpect(status().isUnauthorized())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserCredentialsNotValidException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(USER_CREDENTIALS_NOT_VALID)));
    
        verify(userDetailsService).loadUserByUsername("test.user@domain.com");
        verify(userDetailsService, never()).uploadAvatar("TU00001", avatar);
    }

    @Test
    @DisplayName("Should return NOT_FOUND Response and resolve exception when UserIdNotFoundException was thrown when calling the upload-avatar endpoint")
    void uploadAvatar_UserIdNotFoundExceptionThrown_ResolveExceptionAndNotFoundResponse() throws Exception {
        UserEntity user = UserEntity.builder()
            .userId("TU00001")
            .firstName("Test")
            .lastName("User")
            .email("test.user@domain.com")
            .build();

        MockMultipartFile avatar = new MockMultipartFile(
                "avatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());

        when(userDetailsService.loadUserByUsername("test.user@domain.com")).thenReturn(user);
        doThrow(new UserIdNotFoundException("TU00001")).when(userDetailsService).uploadAvatar("TU00001", avatar);

        mockMvc.perform(multipart("/users/upload-avatar")
                .file(avatar))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserIdNotFoundException.class))
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(String.format(USER_WITH_ID_NOT_FOUND, "TU00001")));
    
        verify(userDetailsService).loadUserByUsername("test.user@domain.com");
        verify(userDetailsService).uploadAvatar("TU00001", avatar);
    }
}
