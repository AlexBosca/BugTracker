package com.example.backend.exception.user;

import com.example.backend.exception.BaseRuntimeException;

import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.backend.util.ExceptionUtilities.USER_CREDENTIALS_NOT_VALID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST)
public class UserCredentialsNotValidException extends BaseRuntimeException {
    
    public UserCredentialsNotValidException() {
        super(USER_CREDENTIALS_NOT_VALID);
    }
}
