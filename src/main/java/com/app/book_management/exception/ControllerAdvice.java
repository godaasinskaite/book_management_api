package com.app.book_management.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler for the application.
 * This class provides a centralized way to manage application-specific exceptions
 * and format the error responses returned to the client.
 */
@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    /**
     * Handles ApplicationException and returns a structured error response.
     *
     * @param e the ApplicationException that was thrown.
     * @return a ResponseEntity containing the error response and the
     *         corresponding HTTP status code.
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .errorCode(e.getErrorCode())
                .time(LocalDateTime.now())
                .build();

        log.error(e.getMessage() + " - error occurred");
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
    }
}
