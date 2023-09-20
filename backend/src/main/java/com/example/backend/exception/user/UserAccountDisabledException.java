package com.example.backend.exception.user;

import static com.example.backend.util.ExceptionUtilities.USER_ACOCUNT_DISABLED;

import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.backend.exception.BaseRuntimeException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(value = UNAUTHORIZED)
public class UserAccountDisabledException extends BaseRuntimeException {

    public UserAccountDisabledException() {
        super(USER_ACOCUNT_DISABLED);
    }
}
