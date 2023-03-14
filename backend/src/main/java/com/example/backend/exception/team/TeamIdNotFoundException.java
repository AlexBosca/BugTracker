package com.example.backend.exception.team;

import com.example.backend.exception.BaseRuntimeException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.backend.util.ExceptionUtilities.TEAM_WITH_ID_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class TeamIdNotFoundException extends BaseRuntimeException {

    public TeamIdNotFoundException(String id) {
        super(TEAM_WITH_ID_NOT_FOUND, id);
    }
}
