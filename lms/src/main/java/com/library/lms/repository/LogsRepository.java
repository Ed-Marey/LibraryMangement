package com.library.lms.service;

import com.library.lms.entity.Book;
import com.library.lms.entity.Borrow;
import com.library.lms.entity.Logs;
import com.library.lms.repository.BookRepository;
import com.library.lms.repository.BorrowRepository;
import com.library.lms.repository.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Optional<Book> getBookById(Integer id) {
        // Example logic: Logically verify ID before fetching
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        // Example logic: Check for duplicate books before saving
        if (bookRepository.existsById(book.getId())) {
            throw new IllegalArgumentException("Book with the same ID already exists");
        }
        return bookRepository.save(book);
    }
}

@Service
public class BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    public Optional<Borrow> getBorrowById(Integer id) {
        // Validate ID
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return borrowRepository.findById(id);
    }

    public Borrow saveBorrow(Borrow borrow) {
        // Example logic: Validate borrow details before saving
        if (borrow.getBook() == null || borrow.getUser() == null) {
            throw new IllegalArgumentException("Borrow must have a book and a user");
        }
        return borrowRepository.save(borrow);
    }
}

@Service
public class LogsService {

    @Autowired
    private LogsRepository logsRepository;

    public Optional<Logs> getLogById(Integer id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return logsRepository.findById(id);
    }

    public Logs saveLog(Logs log) {
        if (log.getAction() == null || log.getTimestamp() == null) {
            throw new IllegalArgumentException("Log must have an action and a timestamp");
        }
        return logsRepository.save(log);
    }
}

// Controller
package com.library.lms.controller;

import com.library.lms.entity.Book;
import com.library.lms.entity.Borrow;
import com.library.lms.entity.Logs;
import com.library.lms.service.BookService;
import com.library.lms.service.BorrowService;
import com.library.lms.service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {
        try {
            Optional<Book> book = bookService.getBookById(id);
            if (book.isPresent()) {
                return ResponseEntity.ok(book.get());
            } else {
                return ResponseEntity.status(404).body("Book not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            Book savedBook = bookService.saveBook(book);
            return ResponseEntity.ok(savedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getBorrowById(@PathVariable Integer id) {
        try {
            Optional<Borrow> borrow = borrowService.getBorrowById(id);
            if (borrow.isPresent()) {
                return ResponseEntity.ok(borrow.get());
            } else {
                return ResponseEntity.status(404).body("Borrow not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBorrow(@RequestBody Borrow borrow) {
        try {
            Borrow savedBorrow = borrowService.saveBorrow(borrow);
            return ResponseEntity.ok(savedBorrow);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

@RestController
@RequestMapping("/api/logs")
public class LogsController {

    @Autowired
    private LogsService logsService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getLogById(@PathVariable Integer id) {
        try {
            Optional<Logs> log = logsService.getLogById(id);
            if (log.isPresent()) {
                return ResponseEntity.ok(log.get());
            } else {
                return ResponseEntity.status(404).body("Log not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createLog(@RequestBody Logs log) {
        try {
            Logs savedLog = logsService.saveLog(log);
            return ResponseEntity.ok(savedLog);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}