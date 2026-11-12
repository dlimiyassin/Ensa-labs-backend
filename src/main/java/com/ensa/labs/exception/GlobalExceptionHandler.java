package com.ensa.labs.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleResourceAlreadyExists(ResourceAlreadyExistsException exception, WebRequest webRequest) {
        return build(HttpStatus.CONFLICT, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(GlobalException exception, WebRequest webRequest) {
        HttpStatus status = exception.getStatus() != null ? exception.getStatus() : HttpStatus.BAD_REQUEST;
        return build(status, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDenied(AccessDeniedException exception, WebRequest webRequest) {
        return build(HttpStatus.FORBIDDEN, exception.getMessage() != null ? exception.getMessage() : "Access denied", webRequest);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetails> handleAuthentication(AuthenticationException exception, WebRequest webRequest) {
        return build(HttpStatus.UNAUTHORIZED, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDetails> handleResponseStatus(ResponseStatusException exception, WebRequest webRequest) {
        return build(HttpStatus.valueOf(exception.getStatusCode().value()), exception.getReason(), webRequest);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgument(IllegalArgumentException exception, WebRequest webRequest) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage(), webRequest);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrity(DataIntegrityViolationException exception, WebRequest webRequest) {
        log.warn("Data integrity violation", exception);
        return build(HttpStatus.CONFLICT, "The operation conflicts with existing data (duplicate value or record still in use).", webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleUnexpected(Exception exception, WebRequest webRequest) {
        log.error("Unexpected error", exception);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach((FieldError error) -> errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors()
                .forEach(error -> errors.putIfAbsent(error.getObjectName(), error.getDefaultMessage()));
        ErrorDetails body = new ErrorDetails(new Date(), "Validation failed", request.getDescription(false), errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorDetails> build(HttpStatus status, String message, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), message, webRequest.getDescription(false)), status);
    }
}
