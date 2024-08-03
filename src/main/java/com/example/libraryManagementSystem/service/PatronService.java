package com.example.libraryManagementSystem.service;

import com.example.libraryManagementSystem.exception.ResourceNotFoundException;
import com.example.libraryManagementSystem.model.Patron;
import com.example.libraryManagementSystem.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatronService {

    @Autowired
    private PatronRepository patronRepository;

    @Cacheable(value = "patrons", key = "'all'")
    @Transactional(readOnly = true)
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Cacheable(value = "patrons", key = "#id")
    @Transactional(readOnly = true)
    public Optional<Patron> getPatronById(Long id) {
        return patronRepository.findById(id);
    }


    @CachePut(value = "patrons", key = "#patron.id")
    @Transactional
    public Patron createPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @CachePut(value = "patrons", key = "#id")
    @Transactional
    public Patron updatePatron(Long id, Patron patron) {
        if (patronRepository.existsById(id)) {
            patron.setId(id);
            return patronRepository.save(patron);
        }
        throw new ResourceNotFoundException("Patron not found with id: " + id);
    }

    @CacheEvict(value = "patrons", key = "#id")
    @Transactional
    public void deletePatron(Long id) {
        if (patronRepository.existsById(id)) {
            patronRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Patron not found with id: " + id);
        }
    }
}
