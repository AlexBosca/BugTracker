package com.example.backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import static com.example.backend.util.Utilities.formattedString;

@Getter
@Setter
public class BaseRuntimeException extends RuntimeException{

    private HttpStatus status = null;
    private Object data = null;

    public BaseRuntimeException() {
        super();
    }

    public BaseRuntimeException(String message, String... args) {
        super(formattedString(message, args));
    }

    public BaseRuntimeException(String message, HttpStatus status, String... args) {
        this(message, args);

        this.status = status;
    }

    public BaseRuntimeException(String message, HttpStatus status, Object data, String... args) {
        this(message, status, args);

        this.data = data;
    }
}
