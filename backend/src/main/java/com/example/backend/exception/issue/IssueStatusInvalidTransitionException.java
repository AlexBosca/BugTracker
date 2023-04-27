package com.example.backend.exception.issue;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.ISSUE_STATUS_INVALID_TRANSITION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = BAD_REQUEST)
public class IssueStatusInvalidTransitionException extends BaseRuntimeException {

    public IssueStatusInvalidTransitionException(String currentState, String nextState) {
        super(ISSUE_STATUS_INVALID_TRANSITION, currentState, nextState);
    }
}
