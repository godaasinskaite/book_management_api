package com.app.book_management.service;

import com.app.book_management.dto.BookRequestDto;
import com.app.book_management.dto.BookResponseDto;
import com.app.book_management.exception.ApplicationException;
import com.app.book_management.exception.ErrorCode;
import com.app.book_management.mapper.BookMapper;
import com.app.book_management.model.Book;
import com.app.book_management.model.Genre;
import com.app.book_management.repository.BookRepository;
import com.app.book_management.validator.BookValidator;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookValidator bookValidator;

    public Book findBookById(final Long id) throws ApplicationException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Book where id = " + id + " not found", ErrorCode.BOOK_NOT_FOUND_EXCEPTION));
    }

    public BookResponseDto getById(final Long id) throws ApplicationException {
        final Book book = findBookById(id);
        final BookResponseDto bookDto = bookMapper.bookToBookResponseDto(book);
        bookDto.setOverallRating(calculateOverallRating(book.getRatings()));
        return bookDto;
    }

    public void addNewBook(final BookRequestDto dto) throws ApplicationException {
        bookValidator.validateBookRequestDto(dto);
        final Book book = bookMapper.toBook(dto);
        bookRepository.save(book);
        log.info("New book created and saved");
    }

    public void deleteBookById(final Long id) throws ApplicationException {
        final Book book = findBookById(id);
        bookRepository.delete(book);
        log.info("Book where id = " + book.getId() + " successfully deleted");
    }

    public List<Book> findAllBooks() throws ApplicationException {
        final List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            log.error("Can not retrieve books from DB");
            throw new ApplicationException("No books were found", ErrorCode.ZERO_BOOKS_FOUND_EXCEPTION);
        }
        log.info(books.size() + " books found in the DB");
        return books;
    }

    public List<BookResponseDto> getAllBooks() throws ApplicationException {
        List<Book> books = findAllBooks();
        return mapToResponseAndCalculateOverallRatings(books);
    }

    private List<BookResponseDto> mapToResponseAndCalculateOverallRatings(final List<Book> books) throws ApplicationException {
        bookValidator.validateBookList(books);
        final List<BookResponseDto> mappedBooks = bookMapper.booksToBookResponseDtos(books);
        setOverallRatings(mappedBooks, books);
        log.info("Books successfully validated and mapped");
        return mappedBooks;
    }


    public List<BookResponseDto> filterByAuthor(final String author) throws ApplicationException {
        bookValidator.validateStringFilter(author);
        log.info("Given author validated");

        final List<Book> books = findAllBooks();
        final List<Book> filteredBooks = books.stream()
                .filter(book -> book.getAuthor().equals(author))
                .collect(Collectors.toList());
        log.info(filteredBooks.size() + " books were found with author = " + author);
        return mapToResponseAndCalculateOverallRatings(filteredBooks);
    }

    public List<BookResponseDto> searchByKeyword(final String keyword) throws ApplicationException {
        bookValidator.validateStringFilter(keyword);
        log.info("Given keyword validated");

        final List<Book> books = findAllBooks();
        final List<Book> filteredBooks = books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase())
                        || book.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        log.info(filteredBooks.size() + " books were found with keyword = " + keyword);
        return mapToResponseAndCalculateOverallRatings(filteredBooks);
    }

    public List<BookResponseDto> filterByTitle(final String title) throws ApplicationException {
        bookValidator.validateStringFilter(title);
        log.info("Given keyword validated");

        final List<Book> books = findAllBooks();
        List<Book> filteredBooks = books.stream()
                .filter(book -> Objects.equals(book.getTitle(), title))
                .collect(Collectors.toList());
        log.info(filteredBooks.size() + " books were found with title = " + title);
        return mapToResponseAndCalculateOverallRatings(filteredBooks);
    }

    public List<BookResponseDto> filterInPriceRange(final Double minPrice, final Double maxPrice) throws ApplicationException {
        bookValidator.validatePriceRange(minPrice, maxPrice);
        final List<Book> books = findAllBooks();
        final List<Book> filteredBooks = books.stream()
                .filter(book -> book.getPrice() >= minPrice && book.getPrice() <= maxPrice)
                .collect(Collectors.toList());
        log.info(filteredBooks.size() + " books were found where price is between " + minPrice + " and " + maxPrice);
        return mapToResponseAndCalculateOverallRatings(filteredBooks);
    }

    public List<BookResponseDto> filterByYear(final Year year) throws ApplicationException {
        bookValidator.validateBookYear(year);
        final List<Book> books = findAllBooks();
        final List<Book> filteredBooks = books.stream()
                .filter(book -> book.getYear().equals(year))
                .collect(Collectors.toList());
        log.info(filteredBooks.size() + " books were found where date = " + year);
        return mapToResponseAndCalculateOverallRatings(filteredBooks);
    }

    public List<BookResponseDto> filterByGenre(@NotNull final Genre genre) throws ApplicationException {
        final List<Book> books = bookRepository.findBooksByGenre(String.valueOf(genre));
        log.info(books.size() + " books were found where genre = " + genre);
        return mapToResponseAndCalculateOverallRatings(books);
    }

    public void rateBook(final Long bookId, final Integer rating) throws ApplicationException {
        bookValidator.validateBookRating(rating);
        final Book book = findBookById(bookId);
        book.getRatings().add(rating);
        bookRepository.save(book);
        log.info("Book rated and saved");
    }

    public List<BookResponseDto> filterBooksByRatings(final Integer rating) throws ApplicationException {
        bookValidator.validateBookRating(rating);
        final List<Book> books = bookRepository.findBooksByAverageRating(rating);
        log.info(books.size() + " books were found with rating = " + rating);
        return mapToResponseAndCalculateOverallRatings(books);
    }

    private Integer calculateOverallRating(final List<Integer> ratings) {
        log.info("Calculating overall rating");
        return (int) ratings.stream()
                .filter(integer -> integer >= 1 && integer <= 5)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(1);
    }

    private void setOverallRatings(final List<BookResponseDto> mappedBooks, final List<Book> booksFromDb) {
        for (int i = 0; i < mappedBooks.size(); i++) {
            final Book book = booksFromDb.get(i);
            final BookResponseDto dto = mappedBooks.get(i);
            final Integer overallRating = calculateOverallRating(book.getRatings());
            dto.setOverallRating(overallRating);
        }
        log.info("Overall books rating calculated and set");
    }
}
