package com.example.libraryManagementSystem.controller;

import com.example.libraryManagementSystem.model.Patron;
import com.example.libraryManagementSystem.service.PatronService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patrons")
@Validated
public class PatronController {

    @Autowired
    private PatronService patronService;

    @GetMapping
    public List<Patron> getAllPatrons() {
        return patronService.getAllPatrons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) {
        return patronService.getPatronById(id)
                .map(patron -> ResponseEntity.ok().body(patron))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Patron> createPatron(@Valid @RequestBody Patron patron) {
        Patron createdPatron = patronService.createPatron(patron);
        return ResponseEntity.ok().body(createdPatron);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id, @Valid @RequestBody Patron patron) {
        Patron updatedPatron = patronService.updatePatron(id, patron);
        return updatedPatron != null ? ResponseEntity.ok().body(updatedPatron) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        Optional<Patron> patronOptional = patronService.getPatronById(id);
        if (patronOptional.isPresent()) {
            patronService.deletePatron(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
