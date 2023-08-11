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

    @Modifying
    void deleteByUserId(String userId);

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
            "SET user.isAccountExpired = FALSE WHERE user.userId = ?1")
    int setAccountNonExpiredByUserId(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.credentialExpiresOn = ?2 WHERE user.userId = ?1")
    int setCrecentialExpiresOn(String userId, LocalDateTime credentialsExpirationDate);
}
