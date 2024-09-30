package com.app.book_management.mapper;

import com.app.book_management.dto.BookRequestDto;
import com.app.book_management.dto.BookResponseDto;
import com.app.book_management.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper interface dor converting Book entities and theis Data Transfer Objects.
 * This interface uses MapStruct for automatic mapping.
 */
@Mapper(componentModel = "spring")
public interface BookMapper {

    /**
     * Converts a BookRequestDto to a Book entity.
     * @param dto the data transfer object obtained from the controller method.
     * @return Book entity populated with the details from the dto.
     */
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "id", ignore = true)
    Book toBook(BookRequestDto dto);

    /**
     * Converts a list of Book entities to a list of BookResponseDto.
     * @param books the list of Book entities to be converted.
     * @return a list of BookResponseDto containing the details of the books.
     */
    @Mapping(target = "ratings", ignore = true)
    List<BookResponseDto> booksToBookResponseDtos(List<Book> books);

    /**
     * Converts a Book entity to BookResponseDto.
     * @param book the Book entity to be converted.
     * @return a BookResponseDto containing the details of the book.
     */
    @Mapping(target = "overallRating", ignore = true)
    BookResponseDto bookToBookResponseDto(Book book);
}
