package com.example.backend.exception.token;

import com.example.backend.exception.BaseRuntimeException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.backend.util.ExceptionUtilities.TOKEN_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class TokenNotFoundException extends BaseRuntimeException {

    public TokenNotFoundException() {
        super(TOKEN_NOT_FOUND);
    }
}
