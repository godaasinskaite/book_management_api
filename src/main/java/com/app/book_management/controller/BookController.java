package com.app.book_management.controller;

import com.app.book_management.dto.BookRequestDto;
import com.app.book_management.dto.BookResponseDto;
import com.app.book_management.exception.ApplicationException;
import com.app.book_management.model.Genre;
import com.app.book_management.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;

/**
 * Controller for managing book-related operations in the application.
 * Provides endpoints for adding, retrieving, filtering, and rating books.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/management/book")
public class BookController {

    private final BookService bookService;

    /**
     * Retrieves a book by its id.
     *
     * @param id the id of the book to be retrieved
     * @return ResponseEntity containing the book details as a BookResponseDto
     *         and HTTP status 200 OK
     * @throws ApplicationException if the book with the specified ID is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getById(@PathVariable final Long id) throws ApplicationException {
        final var book = bookService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    /**
     * Adds a new book to the system.
     *
     * @param dto the BookRequestDto object containing the details of the book to be added
     * @return ResponseEntity with a success message in JSON format and HTTP status 200 OK
     * @throws ApplicationException if dto does not pass validation
     */

    @PostMapping("/")
    public ResponseEntity<String> addNewBook(@RequestBody final BookRequestDto dto) throws ApplicationException {
        bookService.addNewBook(dto);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Book successfully created\"}");
    }

    /**
     * Deletes a book by its id.
     *
     * @param id the id of the book to be deleted
     * @return ResponseEntity containing the message of successful operation
     *         and HTTP status 200 OK
     * @throws ApplicationException if the book with the specified ID is not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable final Long id) throws ApplicationException {
        bookService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Book successfully deleted\"}");
    }

    /**
     * Retrieves a list of all books in the system.
     *
     * @return ResponseEntity containing a list of BookResponseDto objects and HTTP status 200 OK
     * @throws ApplicationException if there is an error while retrieving the books
     */
    @GetMapping("/")
    public ResponseEntity<List<BookResponseDto>> getAllBooks() throws ApplicationException {
        final var books = bookService.getAllBooks();
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * Filters and retrieves books by a specific author.
     *
     * @param author the name of the author whose books are to be retrieved
     * @return ResponseEntity containing a list of BookResponseDto objects filtered by the author and HTTP status 200 OK
     * @throws ApplicationException if zero books is found for filtering and author does not pass the validation
     */
    @GetMapping("/byAuthor/{author}")
    public ResponseEntity<List<BookResponseDto>> filterByAuthor(@PathVariable final String author) throws ApplicationException {
        final var books = bookService.filterByAuthor(author);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * Searches and retrieves books based on a keyword found in their titles or descriptions.
     *
     * @param keyword the keyword used for searching books
     * @return ResponseEntity containing a list of BookResponseDto objects that match the keyword and HTTP status 200 OK
     * @throws ApplicationException if zero books is found for filtering and keyword does not pass the validation
     */
    @GetMapping("/byKeyword/{keyword}")
    public ResponseEntity<List<BookResponseDto>> searchByKeyword(@PathVariable final String keyword) throws ApplicationException {
        final var books = bookService.searchByKeyword(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * Filters and retrieves books by their title.
     *
     * @param title the title of the books to be filtered
     * @return ResponseEntity containing a list of BookResponseDto objects that match the given title and HTTP status 200 OK
     * @throws ApplicationException if zero books is found for filtering or title does not pass the validation
     */
    @GetMapping("/byTitle/{title}")
    public ResponseEntity<List<BookResponseDto>> filterByTitle(@PathVariable final String title) throws ApplicationException {
        final var books = bookService.filterByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * Filters and retrieves books that fall within a specified price range.
     *
     * @param minPrice the minimum price of the books to be retrieved
     * @param maxPrice the maximum price of the books to be retrieved
     * @return ResponseEntity containing a list of BookResponseDto objects within the specified price range and HTTP status 200 OK
     * @throws ApplicationException if zero books is found for filtering or prices does not pass the validation
     */
    @GetMapping("/priceRange")
    public ResponseEntity<List<BookResponseDto>> filterInPriceRange(@RequestParam final Double minPrice, @RequestParam final Double maxPrice) throws ApplicationException {
        final var books = bookService.filterInPriceRange(minPrice, maxPrice);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * Filters and retrieves books published in a specified year.
     *
     * @param year the year in which the books were published
     * @return ResponseEntity containing a list of BookResponseDto objects published in the specified year and HTTP status 200 OK
     * @throws ApplicationException if zero books is found for filtering or year does not pass the validation
     */
    @GetMapping("/byYear/{year}")
    public ResponseEntity<List<BookResponseDto>> filterByYear(@PathVariable final Year year) throws ApplicationException {
        final var books = bookService.filterByYear(year);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * Filters and retrieves books by a specified genre.
     *
     * @param genre the genre of the books to be retrieved
     * @return ResponseEntity containing a list of BookResponseDto objects that match the specified genre and HTTP status 200 OK
     * @throws ApplicationException if zero books is found for filtering or genre does not pass the validation
     */
    @GetMapping("/byGenre/{genre}")
    public ResponseEntity<List<BookResponseDto>> filterByGenre(@PathVariable final Genre genre) throws ApplicationException {
        final var books = bookService.filterByGenre(genre);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /**
     * Rates a book with a specified rating.
     *
     * @param bookId the ID of the book to be rated
     * @param rating the rating to be assigned to the book
     * @return ResponseEntity with a success message in JSON format and HTTP status 200 OK
     * @throws ApplicationException if book by the specified id is not found or rating does not pass the validation
     */
    @PostMapping("/rate/{bookId}")
    public ResponseEntity<String> rateBook(@PathVariable final Long bookId, @RequestBody Integer rating) throws ApplicationException {
        bookService.rateBook(bookId, rating);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Book successfully rated\"}");
    }

    /**
     * Retrieves a list of books that match a specified rating.
     *
     * @param rating the rating used to filter the books
     * @return ResponseEntity containing a list of BookResponseDto objects that match the specified rating and HTTP status 200 OK
     * @throws ApplicationException if zero books is found for filtering or rating does not pass the validation
     */
    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<BookResponseDto>> getBooksByRating(@PathVariable final Integer rating) throws ApplicationException {
        final var books = bookService.filterBooksByRatings(rating);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }
}
