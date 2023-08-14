package com.example.backend.exception.user;

import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.backend.exception.BaseRuntimeException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static com.example.backend.util.ExceptionUtilities.USER_CREDENTIALS_EXPIRED;

@ResponseStatus(value = UNAUTHORIZED)
public class UserCredentialsExpiredException extends BaseRuntimeException {
    
    public UserCredentialsExpiredException() {
        super(USER_CREDENTIALS_EXPIRED);
    }
}
