package com.app.book_management.dto;

import com.app.book_management.model.Genre;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.Year;

@Data
@Builder
public class BookResponseDto {

    private Long id;
    private String title;
    private String description;
    private Genre genre;
    private String author;
    private Year year;
    private Double price;
    private Integer overallRating;
}
