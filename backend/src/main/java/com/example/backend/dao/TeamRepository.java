package com.example.backend.dao;

import com.example.backend.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    Optional<TeamEntity> findByTeamId(String teamId);

    boolean existsByTeamId(String teamId);

    @Modifying
    void deleteByTeamId(String teamId);
}
