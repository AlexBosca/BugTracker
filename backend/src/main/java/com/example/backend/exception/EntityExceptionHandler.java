package com.example.backend.exception;

import com.example.backend.exception.issue.IssueStatusInvalidTransitionException;
import com.example.backend.exception.registration.*;
import com.example.backend.exception.issue.IssueAlreadyCreatedException;
import com.example.backend.exception.issue.IssueNotFoundException;
import com.example.backend.exception.project.ProjectAlreadyCreatedException;
import com.example.backend.exception.project.ProjectNotFoundException;
import com.example.backend.exception.token.TokenExpiredException;
import com.example.backend.exception.token.TokenNotFoundException;
import com.example.backend.exception.user.UserAccountDisabledException;
import com.example.backend.exception.user.UserCredentialsExpiredException;
import com.example.backend.exception.user.UserCredentialsNotValidException;
import com.example.backend.exception.user.UserEmailNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;
import com.example.backend.exception.user.UserPasswordsNotMatchingException;
import com.example.backend.exception.user.UserRoleNotFoundException;
import com.example.backend.exception.user.UserUnexpectedRoleException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class EntityExceptionHandler {

    private final Clock clock;

    public EntityExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();

        if(exception instanceof MethodArgumentNotValidException) {
            for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
                errors.add(fieldError.getDefaultMessage());
            }
        }

        return buildErrorResponse(errors, BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyConfirmedException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyConfirmedException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotConfirmedException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotConfirmedException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(UserAccountDisabledException.class)
    public ResponseEntity<ErrorResponse> handleAccountDisabledException(Exception exception) {
        return buildErrorResponse(exception, UNAUTHORIZED);
    }

    @ExceptionHandler(UserCredentialsNotValidException.class)
    public ResponseEntity<ErrorResponse> handleCredentialsNotValidException(Exception exception) {
        return buildErrorResponse(exception, UNAUTHORIZED);
    }

    @ExceptionHandler(UserPasswordsNotMatchingException.class)
    public ResponseEntity<ErrorResponse> handlePasswordsNotMatchingException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(UserCredentialsExpiredException.class)
    public ResponseEntity<ErrorResponse> handleCredentialsExpiredException(Exception exception) {
        return buildErrorResponse(exception, UNAUTHORIZED);
    }

    @ExceptionHandler(EmailSendFailException.class)
    public ResponseEntity<ErrorResponse> handleEmailSendFailException(Exception exception) {
        return buildErrorResponse(exception, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<ErrorResponse> handleEmailTakenException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(IssueNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIssueIdNotFoundException(Exception exception) {
        return buildErrorResponse(exception, NOT_FOUND);
    }

    @ExceptionHandler(IssueAlreadyCreatedException.class)
    public ResponseEntity<ErrorResponse> handleIssueAlreadyCreatedException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }
    
    @ExceptionHandler(IssueStatusInvalidTransitionException.class)
    public ResponseEntity<ErrorResponse> handleIssueStatusInvalidTransitionException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProjectNotFoundException(Exception exception) {
        return buildErrorResponse(exception, NOT_FOUND);
    }

    @ExceptionHandler(ProjectAlreadyCreatedException.class)
    public ResponseEntity<ErrorResponse> handleProjectAlreadyCreatedException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(Exception exception) {
        return buildErrorResponse(exception, UNAUTHORIZED);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTokenNotFoundException(Exception exception) {
        return buildErrorResponse(exception, NOT_FOUND);
    }

    @ExceptionHandler(UserEmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserEmailNotFoundException(Exception exception) {
        return buildErrorResponse(exception, NOT_FOUND);
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserIdNotFoundException(Exception exception) {
        return buildErrorResponse(exception, NOT_FOUND);
    }

    @ExceptionHandler(UserRoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserRoleNotFoundException(Exception exception) {
        return buildErrorResponse(exception, NOT_FOUND);
    }

    @ExceptionHandler(UserUnexpectedRoleException.class)
    public ResponseEntity<ErrorResponse> handleUserUnexpectedRoleException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(List<String> errors,
                                                             HttpStatus httpStatus) {
        StringBuilder stringBuilder = new StringBuilder("Validation error: ");

        for(String error : errors) {
            if(errors.indexOf(error) < errors.size() - 2) {
                stringBuilder.append(error);
                stringBuilder.append(", ");
            } else if(errors.indexOf(error) < errors.size() - 1) {
                stringBuilder.append(error);
                stringBuilder.append(" and ");
            } else {
                stringBuilder.append(error);
            }
        }

        return buildErrorResponse(stringBuilder.toString(), httpStatus);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception,
                                                             HttpStatus httpStatus) {
        return buildErrorResponse(exception.getMessage(), httpStatus);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message,
                                                             HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse(
                clock,
                httpStatus,
                message);

//        if(printStackTrace && isTraceOn(request)){
//            exceptionInfo.setStackTrace(ExceptionUtils.getStackTrace(exception));
//        }

        // log.error("An invalid request was rejected for reason: {}", message);

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
