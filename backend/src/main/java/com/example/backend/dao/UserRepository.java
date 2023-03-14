package com.example.backend.dao;

import com.example.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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
            "SET user.isEnabled = TRUE WHERE user.email = ?1")
    int enableAccountByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.isAccountLocked = FALSE WHERE user.email = ?1")
    int unlockAccountByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.isAccountExpired = FALSE WHERE user.email = ?1")
    int setAccountNonExpiredByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity user " +
            "SET user.isCredentialsExpired = FALSE WHERE user.email = ?1")
    int setCredentialsNonExpired(String email);
}
