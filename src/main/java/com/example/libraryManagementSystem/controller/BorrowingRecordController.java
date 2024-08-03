package com.example.libraryManagementSystem.controller;

import com.example.libraryManagementSystem.exception.ResourceNotFoundException;
import com.example.libraryManagementSystem.model.Book;
import com.example.libraryManagementSystem.model.BorrowingRecord;
import com.example.libraryManagementSystem.model.Patron;
import com.example.libraryManagementSystem.service.BorrowingRecordService;
import com.example.libraryManagementSystem.repository.BookRepository;
import com.example.libraryManagementSystem.repository.PatronRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/borrow")
@Validated
public class BorrowingRecordController {

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @GetMapping
    public ResponseEntity<List<BorrowingRecord>> getAllBorrowingRecords() {
        List<BorrowingRecord> records = borrowingRecordService.getAllBorrowingRecords();
        if (records.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingRecord> getBorrowingRecordById(@PathVariable Long id) {
        Optional<BorrowingRecord> borrowingRecordOptional = Optional.ofNullable(borrowingRecordService.getBorrowingRecordById(id));
        if (borrowingRecordOptional.isPresent()) {
            return ResponseEntity.ok(borrowingRecordOptional.get());
        } else {
            throw new ResourceNotFoundException("BorrowingRecord not found with id " + id);
        }
    }

    @PostMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity<String> borrowBook(
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
            return ResponseEntity.badRequest().body("Return date must be after borrow date.");
        }

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(borrowDate);
        borrowingRecord.setReturnDate(returnDate);

        BorrowingRecord savedBorrowingRecord = borrowingRecordService.createBorrowingRecord(borrowingRecord);

        return ResponseEntity.status(201).body("Borrowing record created successfully.");
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<String> returnBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId) {

        Optional<BorrowingRecord> borrowingRecordOptional = Optional.ofNullable(borrowingRecordService.findActiveRecord(bookId, patronId));

        if (borrowingRecordOptional.isPresent()) {
            BorrowingRecord borrowingRecord = borrowingRecordOptional.get();
            borrowingRecord.setReturnDate(LocalDate.now());
            borrowingRecordService.updateBorrowingRecord(borrowingRecord.getId(), borrowingRecord);
            return ResponseEntity.ok("Book returned successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowingRecord(@PathVariable Long id) {
        Optional<BorrowingRecord> borrowingRecordOptional = Optional.ofNullable(borrowingRecordService.getBorrowingRecordById(id));
        if (borrowingRecordOptional.isPresent()) {
            borrowingRecordService.deleteBorrowingRecord(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
