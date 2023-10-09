package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.RegistrationRequest;
import com.example.backend.dto.request.UserRequest;
import com.example.backend.dto.response.UserFullResponse;
import com.example.backend.entity.UserEntity;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.AppUserDetailsService;

import lombok.RequiredArgsConstructor;



import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "users")
@RequiredArgsConstructor
public class UserController {
    
    private final AppUserDetailsService userDetailsService;
    private final MapStructMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserFullResponse>> getAllUsers() {
        List<UserEntity> entities = userDetailsService.loadAllUsers();

        List<UserFullResponse> responses = entities.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
                
        return new ResponseEntity<>(
            responses,
            OK
        );
    }

    @PostMapping(path = "/filter")
    public ResponseEntity<List<UserFullResponse>> getFilteredProjects(@RequestBody FilterCriteria filterCriteria) {
        List<UserEntity> entities = userDetailsService.filterUsers(filterCriteria);

        List<UserFullResponse> responses = entities.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());

        return new ResponseEntity<>(
            responses,
            OK
        );
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserFullResponse> getUser(@PathVariable(name = "userId") String userId) {
        return new ResponseEntity<>(
                mapper.toResponse(userDetailsService.loadUserByUserId(userId)),
                OK
        );
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody RegistrationRequest request) {
        userDetailsService.saveUser(mapper.toEntity(request));
        
        return new ResponseEntity<>(OK);
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<Void> updateUser(
        @PathVariable(name = "userId") String userId,
        @RequestBody UserRequest request) {
        userDetailsService.updateUser(
            userId,
            request
        );

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

        return new ResponseEntity<>(OK);
    }
}
