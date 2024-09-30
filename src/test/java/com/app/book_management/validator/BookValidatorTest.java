package com.app.book_management.validator;

import com.app.book_management.dto.BookRequestDto;
import com.app.book_management.exception.ApplicationException;
import com.app.book_management.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookValidatorTest {

    @InjectMocks
    private BookValidator bookValidator;

    @Test
    void validateBookRequestDto() {
        final BookRequestDto nullBook = null;
        assertThrows(ApplicationException.class, () -> bookValidator.validateBookRequestDto(nullBook));
    }

    @Test
    void validateBookRating_withInvalidRating() {
        final Integer rating = 7;
        assertThrows(ApplicationException.class, () -> bookValidator.validateBookRating(rating));
    }

    @Test
    void validateBookRating_withNullRating() {
        final Integer rating = null;
        assertThrows(ApplicationException.class, () -> bookValidator.validateBookRating(rating));
    }

    @Test
    void validateBookList() {
        final List<Book> books = null;
        assertThrows(ApplicationException.class, () -> bookValidator.validateBookList(books));
    }

    @Test
    void validateStringFilter_whenStringIsEmpty() {
        final String stringFilter = "";
        assertThrows(ApplicationException.class, () -> bookValidator.validateStringFilter(stringFilter));
    }

    @Test
    void validateStringFilter_whenStringIsNull() {
        final String stringFilter = null;
        assertThrows(ApplicationException.class, () -> bookValidator.validateStringFilter(stringFilter));
    }

    @Test
    void validatePriceRange_whenMinPriceIsGreater() {
        final Double minPrice = 15.0;
        final Double maxPrice = 1.0;
        assertThrows(ApplicationException.class, () -> bookValidator.validatePriceRange(minPrice, maxPrice));
    }

    @Test
    void validatePriceRange_withNullPrice() {
        final Double minPrice = null;
        final Double maxPrice = 1.0;
        assertThrows(ApplicationException.class, () -> bookValidator.validatePriceRange(minPrice, maxPrice));
    }

    @Test
    void validatePriceRange_whenMinPriceIsZero() {
        final Double minPrice = 0.0;
        final Double maxPrice = 1.0;
        assertThrows(ApplicationException.class, () -> bookValidator.validatePriceRange(minPrice, maxPrice));
    }

    @Test
    void validateBookYear_withInvalidYear() {
        final Year year = Year.of(2500);
        assertThrows(ApplicationException.class, () -> bookValidator.validateBookYear(year));
    }

    @Test
    void validateBookYear_withNullYear() {
        final Year year = null;
        assertThrows(ApplicationException.class, () -> bookValidator.validateBookYear(year));
    }
}