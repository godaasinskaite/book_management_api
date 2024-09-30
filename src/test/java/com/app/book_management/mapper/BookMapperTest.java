package com.app.book_management.mapper;

import com.app.book_management.dto.BookRequestDto;
import com.app.book_management.dto.BookResponseDto;
import com.app.book_management.model.Book;
import com.app.book_management.model.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    void setBookMapper() {
        bookMapper = Mappers.getMapper(BookMapper.class);
    }

    @Test
    void toBook() {
        final BookRequestDto dto = BookRequestDto.builder()
                .year(Year.of(2000))
                .price(15.99)
                .title("Book title")
                .author("Book author")
                .genre(Genre.FANTASY)
                .description("Book description")
                .build();

        final Book book = bookMapper.toBook(dto);
        assertNotNull(book);
        assertEquals("Book title", book.getTitle());
    }

    @Test
    void booksToBookResponseDtos() {
        final List<Book> books = loadTestBooks();
        final List<BookResponseDto> dtos = bookMapper.booksToBookResponseDtos(books);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
    }

    @Test
    void bookToBookResponseDto() {
        final Book book = loadTestBooks().get(0);
        final BookResponseDto dto = bookMapper.bookToBookResponseDto(book);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
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

        return List.of(book1, book2);
    }
}