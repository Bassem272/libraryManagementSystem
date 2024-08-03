package com.example.libraryManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.libraryManagementSystem.model.Patron;

public interface PatronRepository extends JpaRepository<Patron, Long> {
}
