package com.example.backend.service;

import com.example.backend.dao.UserDao;
import com.example.backend.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.backend.enums.UserRole.VISITOR;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public void createUser(UserEntity user) {
        if(user.getRole() == null) {
            user.setRole(VISITOR);
        }

        userDao.insertUser(user);
    }
}
