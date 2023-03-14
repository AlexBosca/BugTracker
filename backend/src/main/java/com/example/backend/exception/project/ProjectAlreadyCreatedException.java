package com.example.backend.exception.project;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.PROJECT_ALREADY_CREATED;

public class ProjectAlreadyCreatedException extends BaseRuntimeException {
    
    public ProjectAlreadyCreatedException(String id) {
        super(PROJECT_ALREADY_CREATED, id);
    }
}
