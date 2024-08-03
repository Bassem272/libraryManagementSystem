package com.example.libraryManagementSystem;

import com.example.libraryManagementSystem.exception.ResourceNotFoundException;
import com.example.libraryManagementSystem.model.Book;
import com.example.libraryManagementSystem.repository.BookRepository;
import com.example.libraryManagementSystem.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublicationYear(2024);
        book.setIsbn("1234567890123");
    }

    @Test
    public void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book.getTitle(), result.get(0).getTitle());
    }

    @Test
    public void testGetBookByIdFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    public void testGetBookByIdNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });

        assertEquals("Book not found with id: 1", exception.getMessage());
    }

    @Test
    public void testCreateBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.createBook(book);

        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    public void testUpdateBookFound() {
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.updateBook(1L, book);

        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    public void testUpdateBookNotFound() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBook(1L, book);
        });

        assertEquals("Book not found with id: 1", exception.getMessage());
    }

    @Test
    public void testDeleteBookFound() {
        when(bookRepository.existsById(anyLong())).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteBookNotFound() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(1L);
        });

        assertEquals("Book not found with id: 1", exception.getMessage());
    }
}
