package com.example.backend.exception.team;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.TEAM_ALREADY_CREATED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = BAD_REQUEST)
public class TeamAlreadyCreatedException extends BaseRuntimeException {
    
    public TeamAlreadyCreatedException(String id) {
        super(TEAM_ALREADY_CREATED, id);
    }
}
