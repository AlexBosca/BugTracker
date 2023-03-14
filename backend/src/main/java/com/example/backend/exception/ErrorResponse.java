package com.example.backend.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
public class ErrorResponse {

    @JsonFormat(shape = STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private int code;
    private String status;
    private String message;
    private String stackTrace;
    private Object data;

    public ErrorResponse(Clock clock) {
        timestamp = LocalDateTime.now(clock);
    }

    public ErrorResponse(Clock clock, HttpStatus httpStatus, String message) {
        this(clock);

        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
    }

    public ErrorResponse(Clock clock, HttpStatus httpStatus, String message, String stackTrace) {
        this(clock, httpStatus, message);

        this.stackTrace = stackTrace;
    }

    public ErrorResponse(Clock clock, HttpStatus httpStatus, String message, String stackTrace, Object data) {
        this(clock, httpStatus, message, stackTrace);

        this.data = data;
    }
}
