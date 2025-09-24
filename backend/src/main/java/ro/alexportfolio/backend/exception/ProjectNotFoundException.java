package ro.alexportfolio.backend.exception;

import org.springframework.http.HttpStatus;

public class ProjectNotFoundException extends ApplicationException {

    public ProjectNotFoundException() {
        super(ExceptionMessages.PROJECT_NOT_FOUND.getMessage(),
              HttpStatus.NOT_FOUND);
    }
}
