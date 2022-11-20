package com.example.backend.controller;

import com.example.backend.dto.request.TeamRequest;
import com.example.backend.dto.response.TeamFullResponse;
import com.example.backend.mapper.MapStructMapper;
import com.example.backend.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "team")
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final MapStructMapper mapper;

    @GetMapping(path = "/{teamId}")
    public ResponseEntity<TeamFullResponse> getTeam(@PathVariable(name = "teamId") String teamId) {
        return new ResponseEntity<>(
                mapper.toResponse(teamService.getTeamByTeamId(teamId)),
                OK
        );
    }

    @PostMapping
    public ResponseEntity<Void> createTeam(@RequestBody TeamRequest request) {
        teamService.saveTeam(mapper.toEntity(request));

        return new ResponseEntity<>(CREATED);
    }

    @PutMapping(path = "/{teamId}/addColleague/{userId}")
    @RolesAllowed({"SCRUM_MASTER", "PROJECT_MANAGER"})
    public ResponseEntity<Void> addColleagueInTeam(@PathVariable(name = "teamId") String teamId, @PathVariable(name = "userId") String userId) {
        teamService.addColleague(teamId, userId);

        return new ResponseEntity<>(OK);
    }
}
