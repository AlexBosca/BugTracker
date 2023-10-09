package com.example.backend.controller;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.TeamRequest;
import com.example.backend.dto.response.TeamFullResponse;
import com.example.backend.entity.TeamEntity;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.TeamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import static com.example.backend.util.team.TeamUtilities.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "teams")
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final MapStructMapper mapper;

    @GetMapping
    public ResponseEntity<List<TeamFullResponse>> getAllTeams() {
        log.info(TEAM_GET_ALL);

        List<TeamEntity> entities = teamService.getAllTeams();

        List<TeamFullResponse> responses = entities.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
            responses,
            OK
        );
    }

    @PostMapping(path = "/filter")
    public ResponseEntity<List<TeamFullResponse>> getFilteredTeams(@RequestBody FilterCriteria filterCriteria) {
        List<TeamEntity> entities = teamService.filterTeams(filterCriteria);

        List<TeamFullResponse> responses = entities.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());

        return new ResponseEntity<>(
            responses,
            OK
        );
    } 

    @GetMapping(path = "/{teamId}")
    public ResponseEntity<TeamFullResponse> getTeam(@PathVariable(name = "teamId") String teamId) {
        log.info(TEAM_GET_BY_ID, teamId);

        return new ResponseEntity<>(
            mapper.toResponse(teamService.getTeamByTeamId(teamId)),
            OK
        );
    }

    @PostMapping
    public ResponseEntity<Void> createTeam(@RequestBody TeamRequest request) {
        log.info(TEAM_CREATE_NEW);

        teamService.saveTeam(mapper.toEntity(request));

        return new ResponseEntity<>(CREATED);
    }

    @PutMapping(path = "/{teamId}/addColleague/{userId}")
    @RolesAllowed({"SCRUM_MASTER", "PROJECT_MANAGER"})
    public ResponseEntity<Void> addColleagueInTeam(@PathVariable(name = "teamId") String teamId, @PathVariable(name = "userId") String userId) {
        log.info(TEAM_ADD_COLLEAGUE, userId, teamId);
        
        teamService.addColleague(teamId, userId);

        return new ResponseEntity<>(OK);
    }
}
