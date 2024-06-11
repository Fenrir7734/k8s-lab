package com.fenrir.simplebookdatabasesite.exception;

import com.fenrir.simplebookdatabasesite.exception.exceptions.DuplicateCredentialsException;
import com.fenrir.simplebookdatabasesite.exception.exceptions.PasswordMismatchException;
import com.fenrir.simplebookdatabasesite.exception.exceptions.ResourceCreationException;
import com.fenrir.simplebookdatabasesite.exception.exceptions.ResourceNotFoundException;
import com.fenrir.simplebookdatabasesite.exception.message.ConstraintViolationErrorMessage;
import com.fenrir.simplebookdatabasesite.exception.message.ConstraintViolationInfo;
import com.fenrir.simplebookdatabasesite.exception.message.ErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    private static final String CONSTRAINT_VIOLATION_MESSAGE = "Constraint Violation";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ ResourceCreationException.class })
    public ResponseEntity<ErrorMessage> handleResourceCreationException(ResourceCreationException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ConstraintViolationException.class, MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(Exception ex, WebRequest request) {
        List<ConstraintViolationInfo> constraintViolations = null;

        if (ex instanceof ConstraintViolationException cve) {
            constraintViolations = cve.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolationInfo::from)
                    .toList();
        } else if (ex instanceof MethodArgumentNotValidException manve) {
            constraintViolations = manve.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(ConstraintViolationInfo::from)
                    .toList();
        } else {
            handleUnknownException(ex, request);
        }

        ErrorMessage message = new ConstraintViolationErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                CONSTRAINT_VIOLATION_MESSAGE,
                EMPTY,
                constraintViolations
        );
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                ex.getMostSpecificCause().getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ IllegalArgumentException.class, PropertyReferenceException.class })
    public ResponseEntity<ErrorMessage> handlePaginationAndSortingException(RuntimeException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ PasswordMismatchException.class, DuplicateCredentialsException.class })
    public ResponseEntity<ErrorMessage> handleCredentialsException(RuntimeException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ AuthenticationException.class  })
    public ResponseEntity<ErrorMessage> handleAuthenticationException(RuntimeException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ErrorMessage> handleUnknownException(Exception ex, WebRequest request) {
        ex.printStackTrace();

        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                INTERNAL_SERVER_ERROR_MESSAGE,
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

