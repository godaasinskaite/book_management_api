package com.app.book_management.repository;

import com.app.book_management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing Book entities.
 * Extends Spring Data JPa to provide methods for performing CRUD operations
 * and
 * contains custom queries related to books in the library.
 *
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Query to retrieve books based on provided genre.
     * @param genre a genre to filter books by.
     * @return list of Book entities with provided genre.
     */
    @Query(value = "SELECT * FROM book WHERE genre = :genre", nativeQuery = true)
    List<Book> findBooksByGenre(@Param("genre") String genre);

    /**
     * Query to retrieve books based on provided average rating.
     * @param avgRating a rating to filter books by.
     * @return list of Book entities with provided rating.
     */
    @Query(value = "SELECT b FROM Book b WHERE ROUND((SELECT AVG(r) FROM b.ratings r)) = :avgRating")
    List<Book> findBooksByAverageRating(@Param("avgRating") Integer avgRating);
}
