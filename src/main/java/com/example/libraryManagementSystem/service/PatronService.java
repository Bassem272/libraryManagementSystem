package com.example.libraryManagementSystem.service;

import com.example.libraryManagementSystem.model.Patron;
import com.example.libraryManagementSystem.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatronService {

    @Autowired
    private PatronRepository patronRepository;

    @Transactional(readOnly = true)
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Patron> getPatronById(Long id) {
        return patronRepository.findById(id);
    }

    @Transactional
    public Patron createPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @Transactional
    public Patron updatePatron(Long id, Patron patron) {
        if (patronRepository.existsById(id)) {
            patron.setId(id);
            return patronRepository.save(patron);
        }
        return null;
    }

    @Transactional
    public void deletePatron(Long id) {
        if (patronRepository.existsById(id)) {
            patronRepository.deleteById(id);
        }
    }
}
