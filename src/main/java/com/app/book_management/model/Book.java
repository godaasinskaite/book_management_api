package com.app.book_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Book title can not be blank")
    @NotNull
    private String title;

    @NotBlank(message = "Book description can not be blank")
    @NotNull
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @NotBlank(message = "Book author can not be blank")
    @NotNull
    private String author;

    @PastOrPresent(message = "Book year can not be in the future")
    @NotNull
    private Year year;

    @DecimalMin(value = "0.0", inclusive = false, message = "Book price must be greater than 0")
    @NotNull
    private Double price;

    @ElementCollection
    @CollectionTable(name = "book_ratings", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "rating")
    @Builder.Default
    private List<@Min(1) @Max(5) Integer> ratings = new ArrayList<>();
}
