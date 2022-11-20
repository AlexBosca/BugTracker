package com.example.backend.exception.registration;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.PASSWORD_NOT_VALID;

public class PasswordNotValidException extends BaseRuntimeException {

    public PasswordNotValidException() {
        super(PASSWORD_NOT_VALID);
    }
}
