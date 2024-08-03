package com.example.libraryManagementSystem;

import com.example.libraryManagementSystem.model.Book;
import com.example.libraryManagementSystem.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testSaveAndFindBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book = bookRepository.save(book);

        Optional<Book> foundBook = bookRepository.findById(book.getId());

        assertEquals("Test Book", foundBook.get().getTitle());
    }
}
