package com.example.backend.dao;

import com.example.backend.entity.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Long> {

    @Query("SELECT c FROM ConfirmationTokenEntity c " +
            "INNER JOIN c.user u " +
            "WHERE u.userId = ?1")
    Optional<ConfirmationTokenEntity> findByUserId(String userId);

    Optional<ConfirmationTokenEntity> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationTokenEntity c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

    boolean existsByUserId(String userId);
    boolean existsByToken(String token);
}
