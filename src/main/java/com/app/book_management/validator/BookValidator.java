package com.app.book_management.validator;

import com.app.book_management.dto.BookRequestDto;
import com.app.book_management.exception.ApplicationException;
import com.app.book_management.exception.ErrorCode;
import com.app.book_management.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

/**
 * Validator class for validating Book entities and other objects related to BookService operations.
 */
@Service
@Slf4j
public class BookValidator {

    /**
     * Ensures that BookRequestDto is not null.
     * @param dto the data transfer object obtained from the controller method.
     * @throws ApplicationException if BookRequestDto is null.
     */
    public void validateBookRequestDto(final BookRequestDto dto) throws ApplicationException {
        if (dto == null) {
            throw new ApplicationException("Book request is null", ErrorCode.BOOK_REQUEST_DTO_NULL_EXCEPTION);
        }
    }

    /**
     * Ensures that provided rating to rate a book or filter by ratings is not null and fits the requirements.
     * @param rating an Integer to rate a book or filter books by.
     * @throws ApplicationException if the rating is null, less than 1 or greater than 5.
     */
    public void validateBookRating(final Integer rating) throws ApplicationException {
        if (rating == null) {
            throw new ApplicationException("Rating is null", ErrorCode.INVALID_BOOK_RATING_EXCEPTION);
        }
        if (rating < 1 || rating > 5) {
            throw new ApplicationException("Book rating must be between 1 and 5", ErrorCode.INVALID_BOOK_RATING_EXCEPTION);
        }
    }

    /**
     * Ensures that provided Book list is not null.
      * @param books a list of Book entities.
     * @throws ApplicationException if provided Book list is null.
     */
    public void validateBookList(final List<Book> books) throws ApplicationException {
        if (books == null) {
            throw new ApplicationException("Book list is null", ErrorCode.BOOK_LIST_IS_NULL);
        }
    }

    /**
     * Ensures that provided String filter is not null or empty.
     * @param filter String value used in a filter.
     * @throws ApplicationException if the provided filter is null or empty.
     */
    public void validateStringFilter(final String filter) throws ApplicationException {
        if (filter == null || filter.trim().isEmpty()) {
            throw new ApplicationException("Given filter is null or empty", ErrorCode.INVALID_AUTHOR_EXCEPTION);
        }
    }

    /**
     * Ensures that provided prices are not nulls, and fits the requirements.
     * @param minPrice the minimum price to validate.
     * @param maxPrice the maximum price to validate.
     * @throws ApplicationException if either the minimum or maximum price is null,
     *  *         if the minimum price is greater than the maximum price,
     *  *         or if either price is less than or equal to zero.
     */
    public void validatePriceRange(final Double minPrice, final Double maxPrice) throws ApplicationException {
        if (minPrice == null || maxPrice == null) {
            throw new ApplicationException("Minimal price or maximum price is null", ErrorCode.INVALID_PRICE_EXCEPTION);
        }
        if (minPrice > maxPrice) {
            throw new ApplicationException("Minimal price cannot be greater than maximum price", ErrorCode.INVALID_PRICE_EXCEPTION);
        }
        if (minPrice <= 0) {
            throw new ApplicationException("Prices must be greater than 0", ErrorCode.INVALID_PRICE_EXCEPTION);
        }
    }

    /**
     * Ensures that provided year for a book is valid.
     * @param year year the year to validate.
     * @throws ApplicationException if the given year is null or if it is
     *  *         in the future.
     */
    public void validateBookYear(final Year year) throws ApplicationException {
        if (year == null) {
            throw new ApplicationException("Given year is null", ErrorCode.INVALID_BOOK_YEAR_EXCEPTION);
        }
        if (year.isAfter(Year.now())) {
            throw new ApplicationException("Book year can not be in the future", ErrorCode.INVALID_BOOK_YEAR_EXCEPTION);
        }
    }
}
