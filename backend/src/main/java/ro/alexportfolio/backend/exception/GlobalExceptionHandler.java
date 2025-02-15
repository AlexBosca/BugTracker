package ro.alexportfolio.backend.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ro.alexportfolio.backend.dto.response.ErrorResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of(
            IssueNotFoundException.class, HttpStatus.NOT_FOUND,
            ProjectNotFoundException.class, HttpStatus.NOT_FOUND,
            IssueOrProjectNotFoundException.class, HttpStatus.NOT_FOUND
    );
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException e) {
        HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(status).body(new ErrorResponseDTO(status.value(), e.getMessage()));
    }
}
