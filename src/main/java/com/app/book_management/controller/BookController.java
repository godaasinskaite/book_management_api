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

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/management/book")
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getById(@PathVariable final Long id) throws ApplicationException {
        final var book = bookService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @PostMapping("/")
    public ResponseEntity<String> addNewBook(@RequestBody final BookRequestDto dto) throws ApplicationException {
        bookService.addNewBook(dto);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Book successfully created\"}");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable final Long id) throws ApplicationException {
        bookService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Book successfully deleted\"}");
    }

    @GetMapping("/")
    public ResponseEntity<List<BookResponseDto>> getAllBooks() throws ApplicationException {
        final var books = bookService.getAllBooks();
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/byAuthor/{author}")
    public ResponseEntity<List<BookResponseDto>> filterByAuthor(@PathVariable final String author) throws ApplicationException {
        final List<BookResponseDto> books = bookService.filterByAuthor(author);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/byKeyword/{keyword}")
    public ResponseEntity<List<BookResponseDto>> searchByKeyword(@PathVariable final String keyword) throws ApplicationException {
        final var books = bookService.searchByKeyword(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/byTitle/{title}")
    public ResponseEntity<List<BookResponseDto>> filterByTitle(@PathVariable final String title) throws ApplicationException {
        final var books = bookService.filterByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/priceRange")
    public ResponseEntity<List<BookResponseDto>> filterInPriceRange(@RequestParam final Double minPrice, @RequestParam final Double maxPrice) throws ApplicationException {
        final List<BookResponseDto> books = bookService.filterInPriceRange(minPrice, maxPrice);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/byYear/{year}")
    public ResponseEntity<List<BookResponseDto>> filterByYear(@PathVariable final Year year) throws ApplicationException {
        final var books = bookService.filterByYear(year);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/byGenre/{genre}")
    public ResponseEntity<List<BookResponseDto>> filterByGenre(@PathVariable final Genre genre) throws ApplicationException {
        final var books = bookService.filterByGenre(genre);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @PostMapping("/{bookId}/rate")
    public ResponseEntity<String> rateBook(@PathVariable final Long bookId, @RequestBody Integer rating) throws ApplicationException {
        bookService.rateBook(bookId, rating);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Book successfully rated\"}");
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<BookResponseDto>> getBooksByRating(@PathVariable final Integer rating) throws ApplicationException {
        final var books = bookService.filterBooksByRatings(rating);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }
}
