package com.example.backend.exception.project;

import com.example.backend.exception.BaseRuntimeException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.backend.util.ExceptionUtilities.PROJECT_WITH_ID_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class ProjectNotFoundException extends BaseRuntimeException {

    public ProjectNotFoundException(String id) {
        super(PROJECT_WITH_ID_NOT_FOUND, id);
    }
}
