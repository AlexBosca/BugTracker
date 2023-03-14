package com.example.backend.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.backend.entity.TeamEntity;

import lombok.RequiredArgsConstructor;

@Repository("team-jpa")
@RequiredArgsConstructor
public class TeamJPADataAccessService implements TeamDao {
    private final TeamRepository teamRepository;

    @Override
    public void deleteTeamByTeamId(String teamId) {
        teamRepository.deleteByTeamId(teamId);
    }

    @Override
    public boolean existsTeamWithTeamId(String teamId) {
        return teamRepository.existsByTeamId(teamId);
    }

    @Override
    public void insertTeam(TeamEntity team) {
        teamRepository.save(team);
    }

    @Override
    public List<TeamEntity> selectAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Optional<TeamEntity> selectTeamByTeamId(String teamId) {
        return teamRepository.findByTeamId(teamId);
    }

    @Override
    public void updateTeam(TeamEntity team) {
        teamRepository.save(team);
    }
}
