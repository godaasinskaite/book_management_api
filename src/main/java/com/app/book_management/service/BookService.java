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
import java.util.stream.Collectors;

/**
 * Service class for managing books.
 * This class provides functionality to create, retrieve, delete, filter books.
 * Interacts with BookRepository for retrieving and saving data in database,
 * BookMapper to map Book for object mapping,
 * and
 * BookValidator to validate dto objects, lists and filters.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookValidator bookValidator;

    /**
     * Retrieves a Book from database with specified id
     * or
     * Throws custom ApplicationException if Book with the specified id is not found.
     *
     * @param id unique constraint of Book Entity.
     * @return retrieved Book.
     * @throws ApplicationException if Book with specified id can not be found.
     */
    public Book findBookById(final Long id) throws ApplicationException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Book where id = " + id + " not found", ErrorCode.BOOK_NOT_FOUND_EXCEPTION));
    }

    /**
     * Method to retrieve a Book by its id
     * and
     * converts it to a BookResponseDto with average overall rating.
     *
     * @param id unique constraint of Book Entity.
     * @return BookResponseDto representing the Book with the specified id.
     * @throws ApplicationException if Book with specified id can not be found.
     */
    public BookResponseDto getById(final Long id) throws ApplicationException {
        final Book book = findBookById(id);
        final BookResponseDto bookDto = bookMapper.bookToBookResponseDto(book);
        bookDto.setOverallRating(calculateOverallRating(book.getRatings()));
        return bookDto;
    }

    /**
     * Creates and saves a new Book with specified details in BookRequestDto.
     *
     * @param dto contains details about new Book.
     * @throws ApplicationException if BookRequestDto does not fit the requirements in validator method.
     */
    public void addNewBook(final BookRequestDto dto) throws ApplicationException {
        bookValidator.validateBookRequestDto(dto);
        final Book book = bookMapper.toBook(dto);
        bookRepository.save(book);
        log.info("New book created and saved");
    }

    /**
     * Deletes Book entity by the specified id
     *
     * @param id unique constraint of Book Entity.
     * @throws ApplicationException if Book with specified id can not be found.
     */
    public void deleteBookById(final Long id) throws ApplicationException {
        final Book book = findBookById(id);
        bookRepository.delete(book);
        log.info("Book where id = " + book.getId() + " successfully deleted");
    }

    /**
     * Retrieves all Books from database.
     *
     * @return Book list from the database.
     * @throws ApplicationException if Book database is empty or can not retrieve books.
     */
    public List<Book> findAllBooks() throws ApplicationException {
        final List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            log.error("Can not retrieve books from DB");
            throw new ApplicationException("No books were found", ErrorCode.ZERO_BOOKS_FOUND_EXCEPTION);
        }
        log.info(books.size() + " books found in the DB");
        return books;
    }

    /**
     * Retrieves all books from database
     * and
     * maps books to BookResponseDto with average overall rating
     *
     * @return mapped BookResponseDto list.
     * @throws ApplicationException if Book database is empty or can not retrieve books.
     */
    public List<BookResponseDto> getAllBooks() throws ApplicationException {
        List<Book> books = findAllBooks();
        return mapToResponseAndCalculateOverallRatings(books);
    }

    /**
     * validates provided list of books,
     * maps a list of Book entities to a list of BookResponseDto
     * and
     * calculates the overall ratings for each book
     *
     * @param books a list of Book entities to be mapped.
     * @return a list of BookResponseDto containing the mapped books with their overall ratings.
     * @throws ApplicationException if Book list to mapped is null
     */
    public List<BookResponseDto> mapToResponseAndCalculateOverallRatings(final List<Book> books) throws ApplicationException {
        bookValidator.validateBookList(books);
        final List<BookResponseDto> mappedBooks = bookMapper.booksToBookResponseDtos(books);
        setOverallRatings(mappedBooks, books);
        log.info("Books successfully validated and mapped");
        return mappedBooks;
    }

    /**
     * Validates provided author String,
     * retrieves all books from the database,
     * filters the list of books by the specified author
     * and
     * returns the corresponding BookResponseDto representations after calculating their overall ratings.
     *
     * @param author the name of the author to filter books by.
     * @return a list of BookResponseDto containing the mapped books with specified author.
     * @throws ApplicationException if the provided author String is null or empty,
     *                              or
     *                              can not retrieve books from database.
     */
    public List<BookResponseDto> filterByAuthor(final String author) throws ApplicationException {
        bookValidator.validateStringFilter(author);
        log.info("Given author validated");

        final List<Book> filteredBooks = findAllBooks().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
        log.info(filteredBooks.size() + " books were found with author = " + author);
        return mapToResponseAndCalculateOverallRatings(filteredBooks);
    }

    /**
     * Validates provided keyword String,
     * retrieves all books from the database,
     * filters the list of books by the specified keyword
     * and
     * returns the corresponding BookResponseDto representations after calculating their overall ratings.
     *
     * @param keyword the keyword to filter books by.
     * @return a list of BookResponseDto containing the mapped books with specified keyword.
     * @throws ApplicationException if the provided keyword String is null or empty,
     *                              or
     *                              can not retrieve books from database.
     */
    public List<BookResponseDto> searchByKeyword(final String keyword) throws ApplicationException {
        bookValidator.validateStringFilter(keyword);
        log.info("Given keyword validated");

        final List<Book> filteredBooks = findAllBooks().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase())
                        || book.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        log.info(filteredBooks.size() + " books were found with keyword = " + keyword);
        return mapToResponseAndCalculateOverallRatings(filteredBooks);
    }

    /**
     * Validates provided title String,
     * retrieves all books from the database,
     * filters the list of books by the specified title
     * and
     * returns the corresponding BookResponseDto representations after calculating their overall ratings.
     *
     * @param title the title to filter books by.
     * @return a list of BookResponseDto containing the mapped books with specified title.
     * @throws ApplicationException if the provided title String is null or empty,
     *                              or
     *                              can not retrieve books from database.
     */
    public List<BookResponseDto> filterByTitle(final String title) throws ApplicationException {
        bookValidator.validateStringFilter(title);
        log.info("Given title validated");

        List<Book> filteredBooks = findAllBooks().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
        log.info(filteredBooks.size() + " books were found with title = " + title);
        return mapToResponseAndCalculateOverallRatings(filteredBooks);
    }

    /**
     * Validates provided prices, so they would not be null, or minimal price would not be greater than maximum price,
     * retrieves all books from the database,
     * filters the list of books by the specified prices
     * and
     * returns the corresponding BookResponseDto representations after calculating their overall ratings.
     *
     * @param minPrice the minimal price to filter books by.
     * @param maxPrice the maximum price to filter books by.
     * @return a list of BookResponseDto containing the mapped books with specified prices.
     * @throws ApplicationException if the provided prices are null, minimal price is greater than maximum price, minimal price is zero
     *                              or
     *                              can not retrieve books from database.
     */
    public List<BookResponseDto> filterInPriceRange(final Double minPrice, final Double maxPrice) throws ApplicationException {
        bookValidator.validatePriceRange(minPrice, maxPrice);
        log.info("Given prices validated");

        final List<Book> filteredBooks = findAllBooks().stream()
                .filter(book -> book.getPrice() >= minPrice && book.getPrice() <= maxPrice)
                .collect(Collectors.toList());
        log.info(filteredBooks.size() + " books were found where price is between " + minPrice + " and " + maxPrice);
        return mapToResponseAndCalculateOverallRatings(filteredBooks);
    }

    /**
     * Validates provided Year object,
     * retrieves all books from the database,
     * filters the list of books by the specified year
     * and
     * returns the corresponding BookResponseDto representations after calculating their overall ratings.
     *
     * @param year the iso year to filter books by.
     * @return a list of BookResponseDto containing the mapped books with specified year.
     * @throws ApplicationException if the provided year is null or in the future,
     *                              or
     *                              can not retrieve books from database.
     */
    public List<BookResponseDto> filterByYear(final Year year) throws ApplicationException {
        bookValidator.validateBookYear(year);
        log.info("Given year validated");

        final List<Book> filteredBooks = findAllBooks().stream()
                .filter(book -> book.getYear().equals(year))
                .collect(Collectors.toList());
        log.info(filteredBooks.size() + " books were found where date = " + year);
        return mapToResponseAndCalculateOverallRatings(filteredBooks);
    }

    /**
     * Validates provided genre enum,
     * retrieves all books from the database,
     * filters the list of books by the specified genre
     * and
     * returns the corresponding BookResponseDto representations after calculating their overall ratings.
     *
     * @param genre the genre to filter books by.
     * @return a list of BookResponseDto containing the mapped books with specified genre.
     * @throws ApplicationException if the provided genre is null
     *                              or
     *                              can not retrieve books from database.
     */
    public List<BookResponseDto> filterByGenre(@NotNull final Genre genre) throws ApplicationException {
        final List<Book> books = bookRepository.findBooksByGenre(String.valueOf(genre));
        log.info(books.size() + " books were found where genre = " + genre);
        return mapToResponseAndCalculateOverallRatings(books);
    }

    /**
     * Adds a rating to Book and saves entity with the specified id.
     *
     * @param bookId unique constraint of Book Entity.
     * @param rating new rating of specified book.
     * @throws ApplicationException if the rating is null or does not fit the requirements of validator,
     *                              or
     *                              can not retrieve book with specified id.
     */
    public void rateBook(final Long bookId, final Integer rating) throws ApplicationException {
        bookValidator.validateBookRating(rating);
        log.info("Given rating validated");
        final Book book = findBookById(bookId);
        book.getRatings().add(rating);
        bookRepository.save(book);
        log.info("Book rated and saved");
    }

    /**
     * Validates provided Integer rating,
     * retrieves all books from the database,
     * filters the list of books by the specified rating
     * and
     * returns the corresponding BookResponseDto representations after calculating their overall ratings.
     *
     * @param rating the rating to filter books by.
     * @return a list of BookResponseDto containing the mapped books with specified rating.
     * @throws ApplicationException if the rating is null or does not fit the requirements of validator,
     *                              or
     *                              can not retrieve books from database.
     */
    public List<BookResponseDto> filterBooksByRatings(final Integer rating) throws ApplicationException {
        bookValidator.validateBookRating(rating);
        log.info("Given rating validated");
        final List<Book> books = bookRepository.findBooksByAverageRating(rating);
        log.info(books.size() + " books were found with rating = " + rating);
        return mapToResponseAndCalculateOverallRatings(books);
    }

    /**
     * Calculates and average overall ratings based on given ratings.
     *
     * @param ratings a list of ratings to calculate.
     * @return calculated average overall rating.
     */
    private Integer calculateOverallRating(final List<Integer> ratings) {
        log.info("Calculating overall rating");
        return (int) ratings.stream()
                .filter(integer -> integer >= 1 && integer <= 5)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(1);
    }

    /**
     * Calculates and sets the overall ratings for a list of BookResponseFto
     * based on the corresponding Book entities.
     *
     * @param mappedBooks a list of BookResponseDto to be updated with overall ratings.
     * @param booksFromDb a list of Book entities containing a list of ratings.
     */
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
