package com.example.backend.dao;

import com.example.backend.entity.TeamEntity;

import java.util.List;
import java.util.Optional;

public interface TeamDao {
    List<TeamEntity> selectAllTeams();
    Optional<TeamEntity> selectTeamByTeamId(String teamId);
    void insertTeam(TeamEntity team);
    boolean existsTeamWithTeamId(String teamId);
    void deleteTeamByTeamId(String teamId);
    void updateTeam(TeamEntity team);
}
