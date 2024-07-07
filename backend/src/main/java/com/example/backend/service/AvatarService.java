package com.example.backend.service;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AvatarService {
    
    @Value("${avatar.upload.dir}")
    private String uploadDir;

    public String saveAvatar(MultipartFile avatar, String userId) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        log.info(uploadPath.toString());

        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = userId + "_avatar";
        Path filePath = uploadPath.resolve(filename);
        try(var inputStream = avatar.getInputStream()) {
            Files.copy(inputStream, filePath, REPLACE_EXISTING);
            log.info(filePath.toString());
        } catch(IOException exception) {
            throw new UncheckedIOException("Failed to save avatar file", exception);
        }

        return filename;
    }
}
