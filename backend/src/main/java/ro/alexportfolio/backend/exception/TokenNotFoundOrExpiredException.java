package ro.alexportfolio.backend.exception;

import org.springframework.http.HttpStatus;

public class TokenNotFoundOrExpiredException extends ApplicationException {
    
    public TokenNotFoundOrExpiredException() {
        super(ExceptionMessages.TOKEN_NOT_FOUND_OR_EXPIRED.getMessage(),
              HttpStatus.BAD_REQUEST);
    }
    
}
