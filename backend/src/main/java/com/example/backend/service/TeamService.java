package com.example.backend.service;

import com.example.backend.dao.TeamDao;
import com.example.backend.dao.UserDao;
import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.entity.TeamEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.team.TeamAlreadyCreatedException;
import com.example.backend.exception.team.TeamIdNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.example.backend.util.team.TeamUtilities.*;

@Slf4j
@Service
public class TeamService {
    private final TeamDao teamDao;
    private final UserDao userDao;

    public TeamService(@Qualifier("teamJpa") TeamDao teamDao,
                       @Qualifier("userJpa") UserDao userDao) {
                        
        this.teamDao = teamDao;
        this.userDao = userDao;
    }

    public List<TeamEntity> getAllTeams() {
        log.info(TEAM_REQUEST_ALL);

        List<TeamEntity> teams = teamDao.selectAllTeams();

        log.info(TEAM_RETURN_ALL);

        return teams;
    }

    public List<TeamEntity> filterTeams(FilterCriteria filterCriteria) {
        return teamDao.selectAllFilteredTeams(filterCriteria);
    }

    public TeamEntity getTeamByTeamId(String teamId) {
        log.info(TEAM_REQUEST_BY_ID, teamId);

        TeamEntity team = teamDao
                .selectTeamByTeamId(teamId)
                .orElseThrow(() -> new TeamIdNotFoundException(teamId));

        log.info(TEAM_RETURN);

        return team;
    }

    public void saveTeam(TeamEntity team) {
        log.info(TEAM_CREATE);

        boolean isTeamPresent = teamDao
                .existsTeamWithTeamId(team.getTeamId());

        if(isTeamPresent) {
            throw new TeamAlreadyCreatedException(team.getTeamId());
        }

        teamDao.insertTeam(team);

        log.info(TEAM_CREATED);
    }

    public void addColleague(String teamId, String userId) {
        log.info(TEAM_ADD_COLLEAGUE_BY_ID, userId, teamId);

        TeamEntity team = teamDao
                .selectTeamByTeamId(teamId)
                .orElseThrow(() -> new TeamIdNotFoundException(teamId));

        UserEntity user = userDao
                .selectUserByUserId(userId)
                .orElseThrow(() -> new UserIdNotFoundException(userId));

        team.getColleagues().add(user);

        teamDao.insertTeam(team);

        log.info(TEAM_COLLEAGUE_ADDED);
    }
}
