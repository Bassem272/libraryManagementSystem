package com.example.libraryManagementSystem.repository;

import com.example.libraryManagementSystem.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    // Add custom query methods if necessary
}
