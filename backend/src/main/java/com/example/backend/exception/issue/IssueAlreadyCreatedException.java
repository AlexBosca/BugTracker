package com.example.backend.exception.issue;

import static com.example.backend.util.ExceptionUtilities.ISSUE_ALREADY_CREATED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.backend.exception.BaseRuntimeException;

@ResponseStatus(value = BAD_REQUEST)
public class IssueAlreadyCreatedException extends BaseRuntimeException {

    public IssueAlreadyCreatedException(String id) {
        super(ISSUE_ALREADY_CREATED, id);
    }
}
