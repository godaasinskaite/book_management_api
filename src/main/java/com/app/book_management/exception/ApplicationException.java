package com.app.book_management.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends Exception {

    private final ErrorCode errorCode;

    public ApplicationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
