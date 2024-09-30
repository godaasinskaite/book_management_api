package com.app.book_management.exception;

import lombok.Getter;

/**
 * Custom exception class for application-specific errors.
 */
@Getter
public class ApplicationException extends Exception {

    private final ErrorCode errorCode;

    /**
     * Constructs a new ApplicationException with the specified detail message
     * and error code.
     *
     * @param message the detail message that describes the error.
     * @param errorCode the ErrorCode enum that specifies the error that occurred.
     */
    public ApplicationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
