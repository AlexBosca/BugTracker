package com.example.backend.exception.user;

import com.example.backend.exception.BaseRuntimeException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.backend.util.ExceptionUtilities.USER_WITH_EMAIL_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class UserEmailNotFoundException extends BaseRuntimeException {

    public UserEmailNotFoundException(String email) {
        super(USER_WITH_EMAIL_NOT_FOUND, email);
    }
}
