package com.example.libraryManagementSystem.service;

import com.example.libraryManagementSystem.exception.ResourceNotFoundException;
import com.example.libraryManagementSystem.model.Book;
import com.example.libraryManagementSystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Cacheable(value = "books", key = "'all'")
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Cacheable(value = "books", key = "#id")
    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @CachePut(value = "books", key = "#book.id")
    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @CachePut(value = "books", key = "#id")
    @Transactional
    public Book updateBook(Long id, Book book) {
        if (bookRepository.existsById(id)) {
            book.setId(id);
            return bookRepository.save(book);
        }
        throw new ResourceNotFoundException("Book not found with id: " + id);
    }

    @CacheEvict(value = "books", key = "#id")
    @Transactional
    public void deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
    }
}
