package com.looyt.usermanagementservice.exception.handler;

import com.looyt.usermanagementservice.dto.response.ErrorResponse;
import com.looyt.usermanagementservice.exception.DuplicateFieldException;
import com.looyt.usermanagementservice.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildErrorResponse(
            HttpStatus status, String message, HttpServletRequest request
    ) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException exception, HttpServletRequest request
    ) {
        log.warn("User not found: {}", exception.getMessage());
        ErrorResponse body = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateFieldException(
            DuplicateFieldException exception, HttpServletRequest request
    ) {
        log.warn("Duplicate field: {}", exception.getMessage());
        ErrorResponse body = buildErrorResponse(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception, HttpServletRequest request
    ) {
        log.warn("Type mismatch: {}", exception.getMessage());
        String message = String.format(
                "Invalid value '%s' for parameter '%s'.",
                exception.getValue(), exception.getName()
        );
        ErrorResponse body = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception, HttpServletRequest request
    ) {
        log.warn("Validation error: {}", exception.getMessage());
        String errors = buildErrorMessage(exception);
        ErrorResponse body = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                errors,
                request
        );
        return ResponseEntity.badRequest().body(body);
    }

    private String buildErrorMessage(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception, HttpServletRequest request
    ) {
        log.warn("Constraint violation: {}", exception.getMessage());
        ErrorResponse body = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadJson(
            HttpMessageNotReadableException exception, HttpServletRequest request
    ) {
        log.warn("Bad JSON: {}", exception.getMessage());
        ErrorResponse body = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON request: " + exception.getMostSpecificCause().getMessage(),
                request
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception exception, HttpServletRequest request
    ) {
        log.error("Unexpected server error at {}: {}", request.getRequestURI(), exception.getMessage(), exception);
        ErrorResponse body = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected server error.",
                request
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
