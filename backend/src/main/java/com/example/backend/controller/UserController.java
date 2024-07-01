package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.RegistrationRequest;
import com.example.backend.dto.request.UserRequest;
import com.example.backend.dto.response.UserFullResponse;
import com.example.backend.entity.UserEntity;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.AppUserDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static com.example.backend.util.user.UserLoggingMessages.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "users")
@RequiredArgsConstructor
public class UserController {
    
    private final AppUserDetailsService userDetailsService;
    private final MapStructMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserFullResponse>> getAllUsers() {
        List<UserEntity> users = userDetailsService.loadAllUsers();
        logInfo(USER_ALL_RETRIEVED, users);

        List<UserFullResponse> responses = users.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
                
        return new ResponseEntity<>(responses, OK);
    }

    @PostMapping(path = "/filter")
    public ResponseEntity<List<UserFullResponse>> getFilteredUsers(@RequestBody FilterCriteria filterCriteria) {
        List<UserEntity> users = userDetailsService.filterUsers(filterCriteria);
        logInfo(USER_FILTERED_RETRIEVED, users);

        List<UserFullResponse> responses = users.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());

        return new ResponseEntity<>(responses, OK);
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserFullResponse> getUser(@PathVariable(name = "userId") String userId) {
        UserEntity user = userDetailsService.loadUserByUserId(userId);
        logInfo(USER_RETRIEVED, user);

        UserFullResponse userResponse = mapper.toResponse(user);

        return new ResponseEntity<>(userResponse, OK);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody RegistrationRequest request) {
        UserEntity user = mapper.toEntity(request);

        userDetailsService.saveUser(user);
        logInfo(USER_CREATED, user);
        
        return new ResponseEntity<>(CREATED);
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable(name = "userId") String userId, @RequestBody UserRequest request) {
        userDetailsService.updateUser(userId, request);
        logInfo(USER_UPDATED, userId);

        return new ResponseEntity<>(OK);
    }

    @PostMapping(path = "/upload-avatar")
    public ResponseEntity<Void> updateAvatar(@RequestPart("avatar") MultipartFile avatar) {
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(userEmail);

        try {
            userDetailsService.uploadAvatar(user.getUserId(), avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/enable/{userId}")
    public ResponseEntity<Void> enableAccount(@PathVariable(name = "userId") String userId) {
        userDetailsService.enableAppUser(userId);

        return new ResponseEntity<>(OK);
    }
    
    @PutMapping(path = "/disable/{userId}")
    public ResponseEntity<Void> disableAccount(@PathVariable(name = "userId") String userId) {
        userDetailsService.disableAppUser(userId);

        return new ResponseEntity<>(OK);
    }

    // @PutMapping(path = "/lock/{userId}")
    // public ResponseEntity<Void> lockAccount(@PathVariable(name = "userId") String userId) {
    //     userDetailsService.(userId);

    //     return new ResponseEntity<>(OK);
    // }

    @PutMapping(path = "/unlock/{userId}")
    public ResponseEntity<Void> unlockAccount(@PathVariable(name = "userId") String userId) {
        userDetailsService.unlockAppUser(userId);

        return new ResponseEntity<>(OK);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") String userId) {
        userDetailsService.deleteUserByUserId(userId);
        logInfo(USER_DELETED, userId);

        return new ResponseEntity<>(OK);
    }

    private void logInfo(String var1, Object var2) {
        log.info(var1, "Controller", var2);
    }
}
