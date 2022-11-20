package com.example.backend.exception.issue;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.ISSUE_STATUS_INVALID_TRANSITION;

public class IssueStatusInvalidTransitionException extends BaseRuntimeException {

    public IssueStatusInvalidTransitionException(String currentState, String nextState) {
        super(ISSUE_STATUS_INVALID_TRANSITION, currentState, nextState);
    }
}
