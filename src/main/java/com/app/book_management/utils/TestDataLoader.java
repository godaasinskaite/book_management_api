package com.app.book_management.utils;

import com.app.book_management.model.Book;
import com.app.book_management.model.Genre;
import com.app.book_management.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        Book book1 = Book.builder()
                .title("Harry Potter and the Chamber of Secrets")
                .description("The Chamber of Secrets was home to an ancient Basilisk.")
                .author("J. K. Rowling")
                .genre(Genre.FANTASY)
                .year(Year.of(2000))
                .price(21.99)
                .ratings(List.of(3, 5, 5, 2, 4, 5, 5))
                .build();

        Book book2 = Book.builder()
                .title("The Lord of the Rings: The Fellowship of the Ring")
                .description("An epic fantasy novel that tells the story of the first part of the journey of Frodo Baggins and his companions to destroy the One Ring.")
                .author("J. R. R. Tolkien")
                .genre(Genre.FANTASY)
                .year(Year.of(1954))
                .price(25.99)
                .ratings(List.of(5, 5, 4, 4, 5, 5, 5))
                .build();

        Book book3 = Book.builder()
                .title("To Kill a Mockingbird")
                .description("Harper Lee's Pulitzer Prize-winning novel about racial inequality and moral growth in the American South.")
                .author("Harper Lee")
                .genre(Genre.HISTORY)
                .year(Year.of(1960))
                .price(15.99)
                .ratings(List.of(5, 5, 4, 5, 5, 5, 4))
                .build();

        Book book4 = Book.builder()
                .title("1984")
                .description("George Orwell's dystopian novel about totalitarianism and government surveillance.")
                .author("George Orwell")
                .genre(Genre.SCIENCE_FICTION)
                .year(Year.of(1949))
                .price(12.99)
                .ratings(List.of(4, 4, 5, 5, 5, 3, 4))
                .build();

        Book book5 = Book.builder()
                .title("Pride and Prejudice")
                .description("Jane Austen's classic novel that deals with issues of class, marriage, and social standing in 19th-century England.")
                .author("Jane Austen")
                .genre(Genre.ROMANCE)
                .year(Year.of(1813))
                .price(9.99)
                .ratings(List.of(5, 4, 4, 4, 5, 5, 5))
                .build();

        Book book6 = Book.builder()
                .title("The Great Gatsby")
                .description("F. Scott Fitzgerald's classic novel about wealth, love, and the American Dream during the Roaring Twenties.")
                .author("F. Scott Fitzgerald")
                .genre(Genre.MYSTERY)
                .year(Year.of(1925))
                .price(14.99)
                .ratings(List.of(5, 5, 4, 3, 4, 4, 5))
                .build();

        Book book7 = Book.builder()
                .title("Moby-Dick")
                .description("Herman Melville's epic tale of obsession and revenge on the high seas, following Captain Ahab's pursuit of the great white whale.")
                .author("Herman Melville")
                .genre(Genre.FICTION)
                .year(Year.of(1851))
                .price(11.99)
                .ratings(List.of(1, 1, 1, 1, 1, 2, 2))
                .build();

        Book book8 = Book.builder()
                .title("The Catcher in the Rye")
                .description("J.D. Salinger's novel about the experiences and disillusionments of teenage rebel Holden Caulfield.")
                .author("J.D. Salinger")
                .genre(Genre.MYSTERY)
                .year(Year.of(1951))
                .price(10.99)
                .ratings(List.of(3, 4, 1, 1, 1, 2, 5))
                .build();

        bookRepository.saveAll(List.of(book1, book2, book3, book4, book5, book6, book7, book8));
    }
}
