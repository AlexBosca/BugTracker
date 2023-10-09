package com.example.backend.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.filter.FilterUtility;
import com.example.backend.entity.TeamEntity;

@Repository("team-jpa")
public class TeamJPADataAccessService implements TeamDao {
    private final FilterUtility<TeamEntity> filterUtility;
    private final TeamRepository teamRepository;

    public TeamJPADataAccessService(EntityManager entityManager, TeamRepository teamRepository) {
        this.filterUtility = new FilterUtility<>(entityManager, TeamEntity.class);
        this.teamRepository = teamRepository;
    }

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
    public List<TeamEntity> selectAllFilteredTeams(FilterCriteria filterCriteria) {
        return filterUtility.filterEntities(filterCriteria);
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
