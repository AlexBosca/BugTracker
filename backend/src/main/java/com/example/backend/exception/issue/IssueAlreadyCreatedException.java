package com.example.backend.exception.issue;

import static com.example.backend.util.ExceptionUtilities.ISSUE_ALREADY_CREATED;

import com.example.backend.exception.BaseRuntimeException;

public class IssueAlreadyCreatedException extends BaseRuntimeException {

    public IssueAlreadyCreatedException(String id) {
        super(ISSUE_ALREADY_CREATED, id);
    }
}
