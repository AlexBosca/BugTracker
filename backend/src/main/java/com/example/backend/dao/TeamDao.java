package com.example.backend.dao;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.entity.TeamEntity;

import java.util.List;
import java.util.Optional;

public interface TeamDao {
    List<TeamEntity> selectAllTeams();
    List<TeamEntity> selectAllFilteredTeams(FilterCriteria filterCriteria);
    Optional<TeamEntity> selectTeamByTeamId(String teamId);
    void insertTeam(TeamEntity team);
    boolean existsTeamWithTeamId(String teamId);
    void deleteTeamByTeamId(String teamId);
    void updateTeam(TeamEntity team);
}
