package com.example.backend.exception.user;

import com.example.backend.exception.BaseRuntimeException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.backend.util.ExceptionUtilities.USER_WITH_ID_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class UserIdNotFoundException extends BaseRuntimeException {

    public UserIdNotFoundException(String... args) {
        super(USER_WITH_ID_NOT_FOUND, args);
    }
}
