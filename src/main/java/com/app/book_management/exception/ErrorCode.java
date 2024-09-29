package com.app.book_management.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    BOOK_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND),
    ZERO_BOOKS_FOUND_EXCEPTION(HttpStatus.NOT_FOUND),
    BOOK_REQUEST_DTO_NULL_EXCEPTION(HttpStatus.BAD_REQUEST),
    INVALID_BOOK_RATING_EXCEPTION(HttpStatus.CONFLICT),
    BOOK_LIST_IS_NULL(HttpStatus.CONFLICT),
    INVALID_AUTHOR_EXCEPTION(HttpStatus.BAD_REQUEST),
    INVALID_PRICE_EXCEPTION(HttpStatus.BAD_REQUEST),
    INVALID_BOOK_YEAR_EXCEPTION(HttpStatus.BAD_REQUEST)
    ;

    private final HttpStatus httpStatus;

    ErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
