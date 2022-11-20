package com.example.backend.service;

import com.example.backend.dao.TeamRepository;
import com.example.backend.dao.UserRepository;
import com.example.backend.entity.TeamEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.team.TeamIdNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeamService {

//    private final static String TEAM_NOT_FOUND_MESSAGE = "Team with id %s not found";
//    private final static String USER_NOT_FOUND_MESSAGE = "User with id %s not found";
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamEntity getTeamByTeamId(String teamId) {
        return teamRepository
                .findByTeamId(teamId)
                .orElseThrow(() -> new TeamIdNotFoundException(teamId));
    }

    public TeamEntity saveTeam(TeamEntity team) {
        return teamRepository.save(team);
    }

    public void addColleague(String teamId, String userId) {
        TeamEntity team = teamRepository
                .findByTeamId(teamId)
                .orElseThrow(() -> new TeamIdNotFoundException(teamId));

        UserEntity user = userRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new UserIdNotFoundException(userId));

        team.getColleagues().add(user);

        teamRepository.save(team);
    }
}
