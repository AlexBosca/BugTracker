package ro.alexportfolio.backend.exception;

import org.springframework.http.HttpStatus;

public class IssueNotFoundException extends ApplicationException {

    public IssueNotFoundException() {
        super(ExceptionMessages.ISSUE_NOT_FOUND.getMessage(),
              HttpStatus.NOT_FOUND);
    }
}
