package com.example.backend.dao;

import com.example.backend.entity.ProjectConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ProjectConfirmationTokenRepository extends JpaRepository<ProjectConfirmationTokenEntity, Long> {

    @Query("SELECT c FROM ProjectConfirmationTokenEntity c " +
            "INNER JOIN c.project p " +
            "WHERE p.projectKey = ?1")
    Optional<ProjectConfirmationTokenEntity> findByProjectKey(String projectKey);

    Optional<ProjectConfirmationTokenEntity> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ProjectConfirmationTokenEntity c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

    boolean existsByProjectKey(String projectKey);
    boolean existsByToken(String token);
}
