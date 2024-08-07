package com.example.backend.dao;

import com.example.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    void deleteByUserId(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user SET " +
            "user.firstName = COALESCE(?2, user.firstName), " +
            "user.lastName = COALESCE(?3, user.lastName), " +
            "user.email = COALESCE(?4, user.email), " +
            "user.password = COALESCE(?5, user.password), " +
            "user.avatarUrl = COALESCE(?6, user.avatarUrl), " +
            "user.phoneNumber = COALESCE(?7, user.phoneNumber), " +
            "user.jobTitle = COALESCE(?8, user.jobTitle), " +
            "user.department = COALESCE(?9, user.department), " +
            "user.timezone = COALESCE(?10, user.timezone) " +
            "WHERE user.email = ?1")
    int updateUser(String email,
                   String firstName, 
                   String lastName, 
                   String newEmail, 
                   String password,
                   String avatarUrl,
                   String phoneNumber,
                   String jobTitle,
                   String department,
                   String timezone);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.isEnabled = TRUE WHERE user.userId = ?1")
    int enableAccountByUserId(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.isEnabled = FALSE WHERE user.userId = ?1")
    int disableAccountByUserId(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.isAccountLocked = FALSE WHERE user.userId = ?1")
    int unlockAccountByUserId(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.isAccountLocked = TRUE WHERE user.email = ?1")
    int lockAccountByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.failedLoginAttempts = 0 WHERE user.email = ?1")
    int resetAccountFailedLoginAttemptsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.failedLoginAttempts = 0 WHERE user.userId = ?1")
    int resetAccountFailedLoginAttemptsByUserId(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.failedLoginAttempts = ?2 WHERE user.email = ?1")
    int setAccountFailedLoginAttemptsByEmail(String email, int attempts);

    @Transactional
    @Query("SELECT user.isAccountLocked FROM UserEntity user WHERE user.email = ?1")
    boolean isAccountLockedByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.isAccountExpired = FALSE WHERE user.userId = ?1")
    int setAccountNonExpiredByUserId(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.credentialExpiresOn = ?2 WHERE user.userId = ?1")
    int setCrecentialExpiresOn(String userId, LocalDateTime credentialsExpirationDate);
}
