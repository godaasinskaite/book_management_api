package com.app.book_management.dto;

import com.app.book_management.model.Genre;
import lombok.Builder;
import lombok.Data;

import java.time.Year;

/**
 * Data Transfer Object class for representing book information in responses.
 */
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
