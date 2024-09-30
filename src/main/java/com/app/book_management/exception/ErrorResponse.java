package com.app.book_management.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Class that represents a structured response for application errors.
 */
@Data
@Builder
public class ErrorResponse {

    private final String message;
    private final ErrorCode errorCode;
    private final LocalDateTime time;
}
