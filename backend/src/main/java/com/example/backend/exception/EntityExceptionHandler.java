package com.example.backend.exception;

import com.example.backend.exception.issue.IssueStatusInvalidTransitionException;
import com.example.backend.exception.registration.*;
import com.example.backend.exception.issue.IssueIdNotFoundException;
import com.example.backend.exception.project.ProjectIdNotFoundException;
import com.example.backend.exception.team.TeamIdNotFoundException;
import com.example.backend.exception.token.TokenExpiredException;
import com.example.backend.exception.token.TokenNotFoundException;
import com.example.backend.exception.user.UserEmailNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Clock;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class EntityExceptionHandler {

    @Autowired
    private Clock clock;

    @ExceptionHandler(EmailAlreadyConfirmedException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyConfirmedException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotValidException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotValidException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(EmailSendFailException.class)
    public ResponseEntity<ErrorResponse> handleEmailSendFailException(Exception exception) {
        return buildErrorResponse(exception, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<ErrorResponse> handleEmailTakenException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(PasswordNotValidException.class)
    public ResponseEntity<ErrorResponse> handlePasswordNotValidException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(IssueIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIssueIdNotFoundException(Exception exception) {
        return buildErrorResponse(exception, NOT_FOUND);
    }

    @ExceptionHandler(IssueStatusInvalidTransitionException.class)
    public ResponseEntity<ErrorResponse> handleIssueStatusInvalidTransitionException(Exception exception) {
        return buildErrorResponse(exception, BAD_REQUEST);
    }

    @ExceptionHandler(ProjectIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProjectIdNotFoundException(Exception exception) {
        return buildErrorResponse(exception, NOT_FOUND);
    }

    @ExceptionHandler(TeamIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeamIdNotFoundException(Exception exception) {
        return buildErrorResponse(exception, NOT_FOUND);
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

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception,
                                                             HttpStatus httpStatus) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception,
                                                             String message,
                                                             HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse(
                clock,
                httpStatus,
                exception.getMessage());

//        if(printStackTrace && isTraceOn(request)){
//            exceptionInfo.setStackTrace(ExceptionUtils.getStackTrace(exception));
//        }

        log.error("An invalid request was rejected for reason: {}", message);

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
