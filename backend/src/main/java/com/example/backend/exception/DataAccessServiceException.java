package com.example.backend.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = INTERNAL_SERVER_ERROR)
public class DataAccessServiceException extends BaseRuntimeException {
    
    public DataAccessServiceException() {
        super("Error occurred while fetching entities");
    }
}
