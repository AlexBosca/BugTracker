package ro.alexportfolio.backend.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException() {
        super(ExceptionMessages.PROJECT_NOT_FOUND.getMessage());
    }
    
}
