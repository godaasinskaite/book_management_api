package com.app.book_management.dto;

import com.app.book_management.model.Genre;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.Year;

@Data
@Builder
public class BookRequestDto {

    @NotNull
    @NotBlank(message = "Book title can not be blank")
    private String title;

    @NotNull
    @NotBlank(message = "Book description can not be blank")
    private String description;

    @NotNull
    private Genre genre;

    @NotNull
    @NotBlank(message = "Book author can not be blank")
    private String author;

    @NotNull
    @PastOrPresent(message = "Book year can not be in the future")
    private Year year;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Book price must be greater than 0")
    private Double price;
}
