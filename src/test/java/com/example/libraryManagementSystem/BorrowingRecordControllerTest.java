package com.example.libraryManagementSystem;

import com.example.libraryManagementSystem.controller.BorrowingRecordController;
import com.example.libraryManagementSystem.model.Book;
import com.example.libraryManagementSystem.model.BorrowingRecord;
import com.example.libraryManagementSystem.model.Patron;
import com.example.libraryManagementSystem.repository.BookRepository;
import com.example.libraryManagementSystem.repository.PatronRepository;
import com.example.libraryManagementSystem.service.BorrowingRecordService;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(BorrowingRecordController.class)
public class BorrowingRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BorrowingRecordService borrowingRecordService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingRecordController borrowingRecordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBorrowBook() throws Exception {
        Book book = new Book();
        book.setId(1L);
        Patron patron = new Patron();
        patron.setId(1L);

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now());
        borrowingRecord.setReturnDate(LocalDate.now().plusDays(7));

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(borrowingRecordService.createBorrowingRecord(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/1/patron/1")
                        .param("borrowDate", LocalDate.now().toString())
                        .param("returnDate", LocalDate.now().plusDays(7).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.book.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.patron.id").value(1));
    }
}
