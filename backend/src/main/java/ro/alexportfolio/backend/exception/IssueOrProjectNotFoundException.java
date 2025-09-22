package ro.alexportfolio.backend.exception;

import org.springframework.http.HttpStatus;

public class IssueOrProjectNotFoundException extends ApplicationException {

    public IssueOrProjectNotFoundException() {
        super(ExceptionMessages.ISSUE_OR_PROJECT_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
    }
    
}
