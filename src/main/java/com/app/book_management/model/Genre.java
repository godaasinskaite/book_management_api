package com.app.book_management.model;

import lombok.Getter;

/**
 * Represents the different genres of books available in the system.
 * This helps in organizing and filtering books based on their genres.
 */
@Getter
public enum Genre {
    FICTION,
    NON_FICTION,
    MYSTERY,
    FANTASY,
    BIOGRAPHY,
    SCIENCE_FICTION,
    ROMANCE,
    HISTORY
}
