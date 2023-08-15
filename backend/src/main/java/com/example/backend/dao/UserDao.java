package com.example.backend.dao;

import com.example.backend.entity.UserEntity;

import java.time.LocalDateTime;
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
    int enableUserAccountByUserId(String userId);
    int disableUserAccountByUserId(String userId);
    int unlockUserAccountByUserId(String userId);
    int lockUserAccountByEmail(String email);
    int setUserAccountFailedLoginAttemptsByEmail(String email, int attempts);
    int resetUserAccountFailedLoginAttemptsByEmail(String email);
    int resetUserAccountFailedLoginAttemptsByUserId(String userId);
    boolean isUserAccountLockedByEmail(String email);
    int setUserAccountNonExpiredByUserId(String userId);
    int setUserCrecentialsExpiresOn(String userId, LocalDateTime credentialsExpirationDate);
}
