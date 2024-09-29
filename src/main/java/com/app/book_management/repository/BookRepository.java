package com.app.book_management.repository;

import com.app.book_management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM book WHERE genre = :genre", nativeQuery = true)
    List<Book> findBooksByGenre(@Param("genre") String genre);

    @Query(value = "SELECT b FROM Book b WHERE ROUND((SELECT AVG(r) FROM b.ratings r)) = :avgRating")
    List<Book> findBooksByAverageRating(@Param("avgRating") Integer avgRating);
}
