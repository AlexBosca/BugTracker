package ro.alexportfolio.backend.exception;

import java.time.Clock;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ro.alexportfolio.backend.dto.response.ErrorResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Clock clock;

    public GlobalExceptionHandler(final Clock clockParam) {
        this.clock = clockParam;
    }
    
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponseDTO> handleApplicationExceptions(final ApplicationException e) {
        return ResponseEntity.status(e.getHttpStatus())
            .body(new ErrorResponseDTO(e.getHttpStatus().value(),
                                       e.getMessage(),
                                       Instant.now(clock)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(final Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                       e.getMessage(),
                                       Instant.now(clock)));
    }
}
