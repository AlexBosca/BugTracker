package ro.alexportfolio.backend.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException() {
        super(ExceptionMessages.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
    }
    
}
