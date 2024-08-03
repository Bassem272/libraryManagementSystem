package com.example.libraryManagementSystem.service;

import com.example.libraryManagementSystem.model.BorrowingRecord;
import com.example.libraryManagementSystem.repository.BorrowingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowingRecordService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Transactional(readOnly = true)
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<BorrowingRecord> getBorrowingRecordById(Long id) {
        return borrowingRecordRepository.findById(id);
    }

    @Transactional
    public BorrowingRecord createBorrowingRecord(BorrowingRecord borrowingRecord) {
        return borrowingRecordRepository.save(borrowingRecord);
    }

    @Transactional
    public BorrowingRecord updateBorrowingRecord(Long id, BorrowingRecord borrowingRecord) {
        if (borrowingRecordRepository.existsById(id)) {
            borrowingRecord.setId(id);
            return borrowingRecordRepository.save(borrowingRecord);
        }
        return null;
    }

    @Transactional
    public void deleteBorrowingRecord(Long id) {
        if (borrowingRecordRepository.existsById(id)) {
            borrowingRecordRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public Optional<BorrowingRecord> findActiveRecord(Long bookId, Long patronId) {
        return borrowingRecordRepository.findAll().stream()
                .filter(record -> record.getBook().getId().equals(bookId)
                        && record.getPatron().getId().equals(patronId)
                        && record.getReturnDate() == null)
                .findFirst();
    }
}
