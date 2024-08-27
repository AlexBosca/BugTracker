package com.example.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
class AvatarServiceTest {

    @Autowired
    private AvatarService avatarService;
    
    @Value("${avatar.upload.dir}")
    private String uploadDir;

    @Disabled
    @Test
    @DisplayName("Should save avatar at specified path when it exists")
    void saveAvatar_ExistingPath() {
        String userId = "testUser";
        MockMultipartFile avatar = new MockMultipartFile(
                "file", "avatar.jpg", "image/jpeg", "avatar content".getBytes());

        String filename = avatarService.saveAvatar(avatar, userId);

        Path expectedPath = Paths.get(uploadDir).resolve(userId + "_avatar");
        assertThat(filename).isEqualTo(userId + "_avatar");
        assertThat(Files.exists(expectedPath)).isTrue();
    }

    @Disabled
    @Test
    @DisplayName("Should save avatar at specified path when it doesn't exist")
    void saveAvatart_NonexistingPath() throws IOException {
        String userId = "testUser";
        MockMultipartFile avatar = new MockMultipartFile(
                "file", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
        Path expectedPath = Paths.get(uploadDir).resolve(userId + "_avatar");


        Path uploadPath = Paths.get(uploadDir);
        if (Files.exists(uploadPath)) {
            Files.walk(uploadPath)
                .sorted((path1, path2) -> path2.compareTo(path1)) // Reverse order to delete files before directories
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to delete test directory", e);
                    }
                });
        }
        
        assertThat(Files.exists(expectedPath)).isFalse();

        String filename = avatarService.saveAvatar(avatar, userId);

        assertThat(filename).isEqualTo(userId + "_avatar");
        assertThat(Files.exists(expectedPath)).isTrue();
    }

    @Disabled
    @Test
    @DisplayName("Should throw an exception when try to create directories results in an IOException")
    void saveAvatar_createDirectoriesThrowIOException_UncheckedIOExceptionThrown() {
        String userId = "testUser";
        MockMultipartFile avatar = new MockMultipartFile(
                "file", "avatar.jpg", "image/jpeg", "avatar content".getBytes());
                
        Path uploadPath = Paths.get(uploadDir);

        try(MockedStatic<Files> mockedStatic = mockStatic(Files.class)) {

            mockedStatic.when(() -> Files.createDirectories(uploadPath)).thenThrow(new IOException("Simulated IOException"));

            assertThatThrownBy(() -> {
                avatarService.saveAvatar(avatar, userId);
            }).isInstanceOf(UncheckedIOException.class)
            .hasMessage("Failed to create directory where avatar should be saved");
        }
    }

    @Disabled
    @Test
    @DisplayName("Should throw an exception when try to get avatar InputStream results in an IOException")
    void saveAvatar_getInputStreamThrowIOException_UncheckedIOExceptionThrown() throws IOException {
        String userId = "testUser";
        MultipartFile avatar = mock(MultipartFile.class);

        when(avatar.getInputStream()).thenThrow(new IOException("Simulated IOException"));

        assertThatThrownBy(() -> {
            avatarService.saveAvatar(avatar, userId);
        }).isInstanceOf(UncheckedIOException.class)
        .hasMessage("Failed to save avatar file");
    }
}
