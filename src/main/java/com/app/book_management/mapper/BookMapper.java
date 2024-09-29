package com.app.book_management.mapper;

import com.app.book_management.dto.BookRequestDto;
import com.app.book_management.dto.BookResponseDto;
import com.app.book_management.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "ratings", ignore = true)
    Book toBook(BookRequestDto dto);

    @Mapping(target = "ratings", ignore = true)
    List<BookResponseDto> booksToBookResponseDtos(List<Book> books);

    BookResponseDto bookToBookResponseDto(Book book);
}
