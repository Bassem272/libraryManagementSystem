package com.example.libraryManagementSystem;

import com.example.libraryManagementSystem.model.Book;
import com.example.libraryManagementSystem.repository.BookRepository;
import com.example.libraryManagementSystem.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    public BookServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book createdBook = bookService.createBook(book);

        assertEquals("Test Book", createdBook.getTitle());
    }

    @Test
    void testGetBookById() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.getBookById(1L);

        assertEquals(1L, foundBook.get().getId());
    }
}
