package com.example.libraryManagementSystem.service;

import com.example.libraryManagementSystem.exception.ResourceNotFoundException;
import com.example.libraryManagementSystem.model.BorrowingRecord;
import com.example.libraryManagementSystem.repository.BorrowingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowingRecordService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Cacheable(value = "borrowingRecords", key = "'all'")
    @Transactional(readOnly = true)
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordRepository.findAll();
    }

    @Cacheable(value = "borrowingRecords", key = "#id")
    @Transactional(readOnly = true)
    public BorrowingRecord getBorrowingRecordById(Long id) {
        return borrowingRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing record not found with id: " + id));
    }

    @CachePut(value = "borrowingRecords", key = "#borrowingRecord.id")
    @Transactional
    public BorrowingRecord createBorrowingRecord(BorrowingRecord borrowingRecord) {
        return borrowingRecordRepository.save(borrowingRecord);
    }

    @CachePut(value = "borrowingRecords", key = "#id")
    @Transactional
    public BorrowingRecord updateBorrowingRecord(Long id, BorrowingRecord borrowingRecord) {
        if (borrowingRecordRepository.existsById(id)) {
            borrowingRecord.setId(id);
            return borrowingRecordRepository.save(borrowingRecord);
        }
        throw new ResourceNotFoundException("Borrowing record not found with id: " + id);
    }

    @CacheEvict(value = "borrowingRecords", key = "#id")
    @Transactional
    public void deleteBorrowingRecord(Long id) {
        if (borrowingRecordRepository.existsById(id)) {
            borrowingRecordRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Borrowing record not found with id: " + id);
        }
    }

    @Cacheable(value = "borrowingRecords", key = "'active-' + #bookId + '-' + #patronId")
    @Transactional(readOnly = true)
    public BorrowingRecord findActiveRecord(Long bookId, Long patronId) {
        return borrowingRecordRepository.findAll().stream()
                .filter(record -> record.getBook().getId().equals(bookId)
                        && record.getPatron().getId().equals(patronId)
                        && record.getReturnDate() == null)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Active borrowing record not found for bookId: " + bookId + " and patronId: " + patronId));
    }
}
