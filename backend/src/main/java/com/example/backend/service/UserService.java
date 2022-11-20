package com.example.backend.service;

import com.example.backend.dao.UserRepository;
import com.example.backend.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.example.backend.enums.UserRole.VISITOR;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity createUser(UserEntity user) {
        if(user.getRole() == null) {
            user.setRole(VISITOR);
        }

        return userRepository.save(user);
    }
}
