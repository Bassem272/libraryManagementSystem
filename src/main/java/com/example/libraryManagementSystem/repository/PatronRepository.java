package com.example.libraryManagementSystem.repository;

import com.example.libraryManagementSystem.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Long> {
}
