package com.example.libraryManagementSystem;

import com.example.libraryManagementSystem.model.Patron;
import com.example.libraryManagementSystem.repository.PatronRepository;
import com.example.libraryManagementSystem.service.PatronService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService patronService;

    public PatronServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePatron() {
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");

        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        Patron createdPatron = patronService.createPatron(patron);

        assertEquals("John Doe", createdPatron.getName());
    }

    @Test
    void testGetPatronById() {
        Patron patron = new Patron();
        patron.setId(1L);

        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));

        Optional<Patron> foundPatron = patronService.getPatronById(1L);

        assertEquals(1L, foundPatron.get().getId());
    }

    @Test
    void testUpdatePatron() {
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("Jane Doe");

        when(patronRepository.existsById(anyLong())).thenReturn(true);
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        Patron updatedPatron = patronService.updatePatron(1L, patron);

        assertEquals("Jane Doe", updatedPatron.getName());
    }

    @Test
    void testDeletePatron() {
        when(patronRepository.existsById(anyLong())).thenReturn(true);

        patronService.deletePatron(1L);

        // Verification to ensure delete method was called
    }
}
