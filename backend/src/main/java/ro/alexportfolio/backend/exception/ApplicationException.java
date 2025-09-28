package ro.alexportfolio.backend.exception;

import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException {
    private final HttpStatus httpStatus;

    protected ApplicationException(final String message,
                                   final HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
