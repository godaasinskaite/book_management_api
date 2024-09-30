package com.app.book_management.service;

import com.app.book_management.dto.BookResponseDto;
import com.app.book_management.exception.ApplicationException;
import com.app.book_management.mapper.BookMapper;
import com.app.book_management.model.Book;
import com.app.book_management.model.Genre;
import com.app.book_management.repository.BookRepository;
import com.app.book_management.validator.BookValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookValidator bookValidator;
    @Mock
    private BookMapper bookMapper;

    @Test
    void findBookById() {
        assertThrows(ApplicationException.class, () -> bookService.findBookById(null));
    }

    @Test
    void getById() {
        assertThrows(ApplicationException.class, () -> bookService.getById(null));
    }

    @Test
    void deleteBookById() {
        assertThrows(ApplicationException.class, () -> bookService.deleteBookById(null));
    }

    @Test
    void findAllBooks() {
        assertThrows(ApplicationException.class, () -> bookService.findAllBooks());
    }

    @Test
    void getAllBooks() {
        assertThrows(ApplicationException.class, () -> bookService.getAllBooks());
    }

    @Test
    void filterByAuthor() throws ApplicationException {
        final var testBooks = loadTestBooks();
        final var expectedDto = loadTestBookResponseDtos().get(3);

        doNothing().when(bookValidator).validateStringFilter(anyString());
        when(bookRepository.findAll()).thenReturn(testBooks);
        when(bookMapper.booksToBookResponseDtos(anyList())).thenReturn(List.of(expectedDto));

        final var filteredBooks = bookService.filterByAuthor("George Orwell");
        assertEquals(1, filteredBooks.size());
        assertEquals("George Orwell", filteredBooks.get(0).getAuthor());
    }

    @Test
    void searchByKeyword() throws ApplicationException {
        final var testBooks = loadTestBooks();
        final var expectedDto1 = loadTestBookResponseDtos().get(2);
        final var expectedDto2 = loadTestBookResponseDtos().get(3);

        doNothing().when(bookValidator).validateStringFilter(anyString());
        when(bookRepository.findAll()).thenReturn(testBooks);
        when(bookMapper.booksToBookResponseDtos(anyList())).thenReturn(List.of(expectedDto1, expectedDto2));

        final var filteredBooks = bookService.searchByKeyword("in");
        assertEquals(2, filteredBooks.size());
    }

    @Test
    void filterByTitle() throws ApplicationException {
        final var testBooks = loadTestBooks();
        final var expectedDto = loadTestBookResponseDtos().get(2);

        doNothing().when(bookValidator).validateStringFilter(anyString());
        when(bookRepository.findAll()).thenReturn(testBooks);
        when(bookMapper.booksToBookResponseDtos(anyList())).thenReturn(List.of(expectedDto));

        final var filteredBooks = bookService.searchByKeyword("To Kill a Mockingbird");
        assertEquals(1, filteredBooks.size());
    }

    @Test
    void filterInPriceRange() throws ApplicationException {
        final var testBooks = loadTestBooks();
        final var expectedDto1 = loadTestBookResponseDtos().get(0);
        final var expectedDto2 = loadTestBookResponseDtos().get(2);

        doNothing().when(bookValidator).validatePriceRange(anyDouble(), anyDouble());
        when(bookRepository.findAll()).thenReturn(testBooks);
        when(bookMapper.booksToBookResponseDtos(anyList())).thenReturn(List.of(expectedDto1, expectedDto2));

        final var filteredBooks = bookService.filterInPriceRange(15.0, 22.0);
        assertEquals(2, filteredBooks.size());
    }

    @Test
    void filterByYear() throws ApplicationException {
        final var testBooks = loadTestBooks();
        final var expectedDto1 = loadTestBookResponseDtos().get(0);

        doNothing().when(bookValidator).validateBookYear(any());
        when(bookRepository.findAll()).thenReturn(testBooks);
        when(bookMapper.booksToBookResponseDtos(anyList())).thenReturn(List.of(expectedDto1));

        final var filteredBooks = bookService.filterByYear(Year.of(2000));
        assertEquals(1, filteredBooks.size());
    }

    @Test
    void filterByGenre() {
        final var genre = "anotherGenre";
        assertThrows(IllegalArgumentException.class, () -> bookService.filterByGenre(Genre.valueOf(genre)));
    }

    @Test
    void calculateOverallRating() {
        final var ratings = List.of(1, 2, 3, 3, 5, 5, 5);
        final var ratingAvg = calculateOverallRating(ratings);
        assertEquals(3, ratingAvg);
    }

    List<Book> loadTestBooks() {
        Book book1 = Book.builder()
                .id(1L)
                .title("Harry Potter and the Chamber of Secrets")
                .description("The Chamber of Secrets was home to an ancient Basilisk.")
                .author("J. K. Rowling")
                .genre(Genre.FANTASY)
                .year(Year.of(2000))
                .price(21.99)
                .ratings(List.of(3, 5, 5, 2, 4, 5, 5))
                .build();

        Book book2 = Book.builder()
                .id(2L)
                .title("The Lord of the Rings: The Fellowship of the Ring")
                .description("An epic fantasy novel that tells the story of the first part of the journey of Frodo Baggins and his companions to destroy the One Ring.")
                .author("J. R. R. Tolkien")
                .genre(Genre.FANTASY)
                .year(Year.of(1954))
                .price(25.99)
                .ratings(List.of(5, 5, 4, 4, 5, 5, 5))
                .build();

        Book book3 = Book.builder()
                .id(3L)
                .title("To Kill a Mockingbird")
                .description("Harper Lee's Pulitzer Prize-winning novel about racial inequality and moral growth in the American South.")
                .author("Harper Lee")
                .genre(Genre.HISTORY)
                .year(Year.of(1960))
                .price(15.99)
                .ratings(List.of(5, 5, 4, 5, 5, 5, 4))
                .build();

        Book book4 = Book.builder()
                .id(4L)
                .title("1984")
                .description("George Orwell's dystopian novel about totalitarianism and government surveillance.")
                .author("George Orwell")
                .genre(Genre.SCIENCE_FICTION)
                .year(Year.of(1949))
                .price(12.99)
                .ratings(List.of(4, 4, 5, 5, 5, 3, 4))
                .build();

        return List.of(book1, book2, book3, book4);
    }

    List<BookResponseDto> loadTestBookResponseDtos() {
        BookResponseDto book1 = BookResponseDto.builder()
                .id(1L)
                .title("Harry Potter and the Chamber of Secrets")
                .description("The Chamber of Secrets was home to an ancient Basilisk.")
                .author("J. K. Rowling")
                .genre(Genre.FANTASY)
                .year(Year.of(2000))
                .price(21.99)
                .overallRating(calculateOverallRating(List.of(3, 5, 5, 2, 4, 5, 5)))
                .build();

        BookResponseDto book2 = BookResponseDto.builder()
                .id(2L)
                .title("The Lord of the Rings: The Fellowship of the Ring")
                .description("An epic fantasy novel that tells the story of the first part of the journey of Frodo Baggins and his companions to destroy the One Ring.")
                .author("J. R. R. Tolkien")
                .genre(Genre.FANTASY)
                .year(Year.of(1954))
                .price(25.99)
                .overallRating(calculateOverallRating(List.of(5, 5, 4, 4, 5, 5, 5)))
                .build();

        BookResponseDto book3 = BookResponseDto.builder()
                .id(3L)
                .title("To Kill a Mockingbird")
                .description("Harper Lee's Pulitzer Prize-winning novel about racial inequality and moral growth in the American South.")
                .author("Harper Lee")
                .genre(Genre.HISTORY)
                .year(Year.of(1960))
                .price(15.99)
                .overallRating(calculateOverallRating(List.of(5, 5, 4, 5, 5, 5, 4)))
                .build();

        BookResponseDto book4 = BookResponseDto.builder()
                .id(4L)
                .title("1984")
                .description("George Orwell's dystopian novel about totalitarianism and government surveillance.")
                .author("George Orwell")
                .genre(Genre.SCIENCE_FICTION)
                .year(Year.of(1949))
                .price(12.99)
                .overallRating(calculateOverallRating(List.of(4, 4, 5, 5, 5, 3, 4)))
                .build();

        return List.of(book1, book2, book3, book4);
    }

    private Integer calculateOverallRating(final List<Integer> ratings) {
        return (int) ratings.stream()
                .filter(integer -> integer >= 1 && integer <= 5)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(1);
    }
}