package com.example.backend.exception.user;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.USER_PASSWORDS_NOT_MATCHING;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = BAD_REQUEST)
public class UserPasswordsNotMatchingException extends BaseRuntimeException {
    
    public UserPasswordsNotMatchingException() {
        super(USER_PASSWORDS_NOT_MATCHING);
    }
}
