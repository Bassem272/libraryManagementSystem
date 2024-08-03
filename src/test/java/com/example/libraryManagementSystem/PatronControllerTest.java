package com.example.libraryManagementSystem;

import com.example.libraryManagementSystem.controller.PatronController;
import com.example.libraryManagementSystem.model.Patron;
import com.example.libraryManagementSystem.service.PatronService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(PatronController.class)
public class PatronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PatronService patronService;

    @InjectMocks
    private PatronController patronController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllPatrons() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetPatronById() throws Exception {
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");

        when(patronService.getPatronById(anyLong())).thenReturn(Optional.of(patron));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testCreatePatron() throws Exception {
        Patron patron = new Patron();
        patron.setName("Jane Doe");

        when(patronService.createPatron(any(Patron.class))).thenReturn(patron);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    void testUpdatePatron() throws Exception {
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("Updated Name");

        when(patronService.updatePatron(anyLong(), any(Patron.class))).thenReturn(patron);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void testDeletePatron() throws Exception {
        when(patronService.getPatronById(anyLong())).thenReturn(Optional.of(new Patron()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testCreatePatronValidationError() throws Exception {
        Patron invalidPatron = new Patron();
        // Assuming the Patron class has validation constraints that are violated

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatron)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testUpdatePatronNotFound() throws Exception {
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("Non-existent Patron");

        when(patronService.updatePatron(anyLong(), any(Patron.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
