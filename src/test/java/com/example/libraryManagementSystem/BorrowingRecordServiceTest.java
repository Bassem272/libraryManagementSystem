package com.example.libraryManagementSystem;

import com.example.libraryManagementSystem.exception.ResourceNotFoundException;
import com.example.libraryManagementSystem.model.Book;
import com.example.libraryManagementSystem.model.BorrowingRecord;
import com.example.libraryManagementSystem.model.Patron;
import com.example.libraryManagementSystem.repository.BorrowingRecordRepository;
import com.example.libraryManagementSystem.service.BorrowingRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BorrowingRecordServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BorrowingRecordService borrowingRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBorrowingRecords() {
        BorrowingRecord record1 = new BorrowingRecord();
        BorrowingRecord record2 = new BorrowingRecord();
        List<BorrowingRecord> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);

        when(borrowingRecordRepository.findAll()).thenReturn(records);

        List<BorrowingRecord> result = borrowingRecordService.getAllBorrowingRecords();

        assertEquals(2, result.size());
        verify(borrowingRecordRepository, times(1)).findAll();
    }

    @Test
    void getBorrowingRecordById_ExistingId_ReturnsRecord() {
        Long id = 1L;
        BorrowingRecord record = new BorrowingRecord();
        record.setId(id);

        when(borrowingRecordRepository.findById(id)).thenReturn(Optional.of(record));

        BorrowingRecord result = borrowingRecordService.getBorrowingRecordById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getBorrowingRecordById_NonExistingId_ThrowsException() {
        Long id = 1L;

        when(borrowingRecordRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            borrowingRecordService.getBorrowingRecordById(id);
        });

        assertEquals("Borrowing record not found with id: " + id, exception.getMessage());
    }

    @Test
    void createBorrowingRecord_Success() {
        BorrowingRecord record = new BorrowingRecord();
        record.setId(1L);

        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(record);

        BorrowingRecord result = borrowingRecordService.createBorrowingRecord(record);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void updateBorrowingRecord_ExistingId_Success() {
        Long id = 1L;
        BorrowingRecord record = new BorrowingRecord();
        record.setId(id);

        when(borrowingRecordRepository.existsById(id)).thenReturn(true);
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(record);

        BorrowingRecord result = borrowingRecordService.updateBorrowingRecord(id, record);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void updateBorrowingRecord_NonExistingId_ThrowsException() {
        Long id = 1L;
        BorrowingRecord record = new BorrowingRecord();

        when(borrowingRecordRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            borrowingRecordService.updateBorrowingRecord(id, record);
        });

        assertEquals("Borrowing record not found with id: " + id, exception.getMessage());
    }

    @Test
    void deleteBorrowingRecord_ExistingId_Success() {
        Long id = 1L;

        when(borrowingRecordRepository.existsById(id)).thenReturn(true);

        borrowingRecordService.deleteBorrowingRecord(id);

        verify(borrowingRecordRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteBorrowingRecord_NonExistingId_ThrowsException() {
        Long id = 1L;

        when(borrowingRecordRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            borrowingRecordService.deleteBorrowingRecord(id);
        });

        assertEquals("Borrowing record not found with id: " + id, exception.getMessage());
    }

    @Test
    void findActiveRecord_ExistingRecord_ReturnsRecord() {
        Long bookId = 1L;
        Long patronId = 2L;
        BorrowingRecord record = new BorrowingRecord();
        record.setBook(new Book());
        record.getBook().setId(bookId);
        record.setPatron(new Patron());
        record.getPatron().setId(patronId);
        record.setReturnDate(null);
        List<BorrowingRecord> records = new ArrayList<>();
        records.add(record);

        when(borrowingRecordRepository.findAll()).thenReturn(records);

        BorrowingRecord result = borrowingRecordService.findActiveRecord(bookId, patronId);

        assertNotNull(result);
        assertEquals(bookId, result.getBook().getId());
        assertEquals(patronId, result.getPatron().getId());
    }

    @Test
    void findActiveRecord_NonExistingRecord_ThrowsException() {
        Long bookId = 1L;
        Long patronId = 2L;

        when(borrowingRecordRepository.findAll()).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            borrowingRecordService.findActiveRecord(bookId, patronId);
        });

        assertEquals("Active borrowing record not found for bookId: " + bookId + " and patronId: " + patronId, exception.getMessage());
    }
}
