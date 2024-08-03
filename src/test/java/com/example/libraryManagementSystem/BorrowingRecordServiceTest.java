package com.example.libraryManagementSystem;

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
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BorrowingRecordServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BorrowingRecordService borrowingRecordService;

    private BorrowingRecord borrowingRecord;
    private Book book;
    private Patron patron;

    public BorrowingRecordServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);

        patron = new Patron();
        patron.setId(1L);

        borrowingRecord = new BorrowingRecord();
        borrowingRecord.setId(1L);
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now());
    }

    @Test
    void testGetAllBorrowingRecords() {
        List<BorrowingRecord> records = new ArrayList<>();
        records.add(borrowingRecord);
        when(borrowingRecordRepository.findAll()).thenReturn(records);

        List<BorrowingRecord> result = borrowingRecordService.getAllBorrowingRecords();

        assertEquals(1, result.size());
        assertEquals(borrowingRecord, result.get(0));
    }

    @Test
    void testGetBorrowingRecordById() {
        when(borrowingRecordRepository.findById(anyLong())).thenReturn(Optional.of(borrowingRecord));

        Optional<BorrowingRecord> result = borrowingRecordService.getBorrowingRecordById(1L);

        assertTrue(result.isPresent());
        assertEquals(borrowingRecord, result.get());
    }

    @Test
    void testCreateBorrowingRecord() {
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        BorrowingRecord createdRecord = borrowingRecordService.createBorrowingRecord(borrowingRecord);

        assertEquals(borrowingRecord, createdRecord);
    }

    @Test
    void testUpdateBorrowingRecord() {
        when(borrowingRecordRepository.existsById(anyLong())).thenReturn(true);
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        BorrowingRecord updatedRecord = borrowingRecordService.updateBorrowingRecord(1L, borrowingRecord);

        assertEquals(borrowingRecord, updatedRecord);
    }

    @Test
    void testDeleteBorrowingRecord() {
        when(borrowingRecordRepository.existsById(anyLong())).thenReturn(true);

        borrowingRecordService.deleteBorrowingRecord(1L);

        verify(borrowingRecordRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindActiveRecord() {
        borrowingRecord.setReturnDate(null);
        List<BorrowingRecord> records = new ArrayList<>();
        records.add(borrowingRecord);
        when(borrowingRecordRepository.findAll()).thenReturn(records);

        Optional<BorrowingRecord> result = borrowingRecordService.findActiveRecord(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals(borrowingRecord, result.get());
    }
}
