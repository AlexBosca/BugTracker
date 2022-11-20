package com.example.backend.exception.issue;

import com.example.backend.exception.BaseRuntimeException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.backend.util.ExceptionUtilities.ISSUE_WITH_ID_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class IssueIdNotFoundException extends BaseRuntimeException {

    public IssueIdNotFoundException(String id) {
        super(ISSUE_WITH_ID_NOT_FOUND, id);
    }
}
