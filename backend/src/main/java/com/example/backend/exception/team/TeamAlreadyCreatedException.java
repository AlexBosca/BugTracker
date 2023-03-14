package com.example.backend.exception.team;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.TEAM_ALREADY_CREATED;

public class TeamAlreadyCreatedException extends BaseRuntimeException {
    
    public TeamAlreadyCreatedException(String id) {
        super(TEAM_ALREADY_CREATED, id);
    }
}
