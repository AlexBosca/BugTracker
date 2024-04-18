package com.example.backend.dao;

import com.example.backend.entity.UserConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional(readOnly = true)
public interface UserConfirmationTokenRepository extends JpaRepository<UserConfirmationTokenEntity, Long> {

    @Query("SELECT c FROM UserConfirmationTokenEntity c " +
            "INNER JOIN c.user u " +
            "WHERE u.userId = ?1")
    Optional<UserConfirmationTokenEntity> findByUserId(String userId);

    Optional<UserConfirmationTokenEntity> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE UserConfirmationTokenEntity c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

    boolean existsByUserId(String userId);
    boolean existsByToken(String token);
}
