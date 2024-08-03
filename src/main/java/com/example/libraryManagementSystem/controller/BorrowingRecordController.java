package com.example.libraryManagementSystem.controller;

import com.example.libraryManagementSystem.model.Book;
import com.example.libraryManagementSystem.model.BorrowingRecord;
import com.example.libraryManagementSystem.model.Patron;
import com.example.libraryManagementSystem.repository.BookRepository;
import com.example.libraryManagementSystem.repository.PatronRepository;
import com.example.libraryManagementSystem.service.BorrowingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Validated
public class BorrowingRecordController {

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @GetMapping("/borrow")
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordService.getAllBorrowingRecords();
    }

    @GetMapping("/borrow/{id}")
    public ResponseEntity<BorrowingRecord> getBorrowingRecordById(@PathVariable Long id) {
        return borrowingRecordService.getBorrowingRecordById(id)
                .map(borrowingRecord -> ResponseEntity.ok().body(borrowingRecord))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> borrowBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId,
            @RequestParam @PastOrPresent LocalDate borrowDate,
            @RequestParam @NotNull LocalDate returnDate) {

        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<Patron> patronOptional = patronRepository.findById(patronId);

        if (bookOptional.isEmpty() || patronOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Book book = bookOptional.get();
        Patron patron = patronOptional.get();

        if (borrowDate.isAfter(returnDate)) {
            return ResponseEntity.badRequest().body(null); // Return an error response
        }

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(borrowDate);
        borrowingRecord.setReturnDate(returnDate);

        BorrowingRecord savedBorrowingRecord = borrowingRecordService.createBorrowingRecord(borrowingRecord);

        return ResponseEntity.status(201).body(savedBorrowingRecord);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> returnBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId) {

        Optional<BorrowingRecord> borrowingRecordOptional = borrowingRecordService.findActiveRecord(bookId, patronId);

        if (borrowingRecordOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BorrowingRecord borrowingRecord = borrowingRecordOptional.get();
        borrowingRecord.setReturnDate(LocalDate.now());

        BorrowingRecord updatedBorrowingRecord = borrowingRecordService.updateBorrowingRecord(borrowingRecord.getId(), borrowingRecord);
        return ResponseEntity.ok(updatedBorrowingRecord);
    }

    @DeleteMapping("/borrow/{id}")
    public ResponseEntity<Void> deleteBorrowingRecord(@PathVariable Long id) {
        if (borrowingRecordService.getBorrowingRecordById(id).isPresent()) {
            borrowingRecordService.deleteBorrowingRecord(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Global exception handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
