package com.example.backend.dao;

import com.example.backend.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<UserEntity> selectAllUsers();
    Optional<UserEntity> selectUserByUserId(String userId);
    Optional<UserEntity> selectUserByEmail(String email);
    void insertUser(UserEntity user);
    boolean existsUserByUserId(String userId);
    boolean existsUserByEmail(String email);
    void deleteUserByUserId(String userId);
    void updateUser(UserEntity user);
    int enableUserAccountByUserId(String email);
    int disableUserAccountByUserId(String email);
    int unlockUserAccountByEmail(String email);
    int setUserAccountNonExpiredByEmail(String email);
    int setUserCredentialsNonExpired(String email);
}
