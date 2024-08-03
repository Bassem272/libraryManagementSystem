# libraryManagementSystem
Documentaion for the project: 

```markdown
# Library Management System

This is a Spring Boot application for managing library resources, including books, patrons, and borrowing records.

## Table of Contents

- [Requirements](#requirements)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
  - [Books](#books)
  - [Patrons](#patrons)
  - [Borrowing Records](#borrowing-records)
- [Testing](#testing)
- [Documentation](#documentation)

## Requirements

- Java 17 or higher
- Maven 3.6.0 or higher

## Setup

1. **Clone the repository**:
   ```sh
   git clone https://github.com/Bassem272/libraryManagementSystem.git
   cd libraryManagementSystem
   ```

2. **Configure the database**:
   The application uses the H2 in-memory database by default. The configuration is already set in the `application.properties` file:
   ```properties
   spring.application.name=libraryManagementSystem

# DataSource Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# H2 Console Configuration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true

   ```

3. **Build the project**:
   ```sh
   mvn clean install
   ```

## Running the Application

1. **Run the Spring Boot application**:
   ```sh
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080`.

2. **Access the H2 console**:
   You can access the H2 console at `http://localhost:8080/h2-console`. Use the following settings to log in:
    - **JDBC URL**: `jdbc:h2:mem:librarydb`
    - **Username**: `sa`
    - **Password**: `password`

## API Endpoints

### Books

- **Get all books**:
  ```http
  GET /api/books
  ```

- **Get a book by ID**:
  ```http
  GET /api/books/{id}
  ```

- **Create a new book**:
  ```http
  POST /api/books
  Content-Type: application/json

  {
    "title": "Book Title",
    "author": "Author Name",
    "publicationYear": 2020,
    "isbn": "123-4567890123"
  }
  ```

- **Update a book**:
  ```http
  PUT /api/books/{id}
  Content-Type: application/json

  {
    "title": "Updated Title",
    "author": "Updated Author",
    "publicationYear": 2021,
    "isbn": "123-4567890123"
  }
  ```

- **Delete a book**:
  ```http
  DELETE /api/books/{id}
  ```

### Patrons

- **Get all patrons**:
  ```http
  GET /api/patrons
  ```

- **Get a patron by ID**:
  ```http
  GET /api/patrons/{id}
  ```

- **Create a new patron**:
  ```http
  POST /api/patrons
  Content-Type: application/json

  {
    "name": "Patron Name",
    "contactInfo": "patron@example.com"
  }
  ```

- **Update a patron**:
  ```http
  PUT /api/patrons/{id}
  Content-Type: application/json

  {
    "name": "Updated Name",
    "contactInfo": "updated@example.com"
  }
  ```

- **Delete a patron**:
  ```http
  DELETE /api/patrons/{id}
  ```

### Borrowing Records

- **Get all borrowing records**:
  ```http
  GET /api/borrow
  ```

- **Get a borrowing record by ID**:
  ```http
  GET /api/borrow/{id}
  ```

- **Create a new borrowing record**:
  ```http
  POST /api/borrow/{bookId}/patron/{patronId}
  Content-Type: application/json

  {
    "borrowDate": "2023-08-01",
    "returnDate": "2023-08-15"
  }
  ```

- **Update a borrowing record (return book)**:
  ```http
  PUT /api/return/{bookId}/patron/{patronId}
  ```

- **Delete a borrowing record**:
  ```http
  DELETE /api/borrow/{id}
  ```

## Testing

### Running Tests

Unit tests are provided to validate the functionality of the services. The tests use JUnit, Mockito, and Spring Boot Test.

To run the tests:
```sh
mvn test
```

### Example Test for `BorrowingRecordService`

```java
package com.example.libraryManagementSystem.service;

import com.example.libraryManagementSystem.model.BorrowingRecord;
import com.example.libraryManagementSystem.repository.BorrowingRecordRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BorrowingRecordServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BorrowingRecordService borrowingRecordService;

    public BorrowingRecordServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBorrowingRecord() {
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setId(1L);

        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        BorrowingRecord createdBorrowingRecord = borrowingRecordService.createBorrowingRecord(borrowingRecord);

        assertEquals(1L, createdBorrowingRecord.getId());
    }

    @Test
    void testGetBorrowingRecordById() {
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setId(1L);

        when(borrowingRecordRepository.findById(anyLong())).thenReturn(Optional.of(borrowingRecord));

        Optional<BorrowingRecord> foundBorrowingRecord = borrowingRecordService.getBorrowingRecordById(1L);

        assertEquals(1L, foundBorrowingRecord.get().getId());
    }
}
```

## Documentation

### Running the Application

To start the application, use:
```sh
mvn spring-boot:run
```

### Interacting with API Endpoints

Use tools like Postman or curl to interact with the API endpoints. Each endpoint's details are provided in the [API Endpoints](#api-endpoints) section.

### Authentication

Currently, no authentication mechanism is implemented. This can be extended as needed using Spring Security.

---

This document provides a comprehensive guide to set up, run, and test the Library Management System application. For further details, refer to the Spring Boot documentation.
```

Save this content into a file named `README.md` and place it in the root directory of your project. This file will provide clear instructions on setting up, running, and interacting with your Library Management System application.