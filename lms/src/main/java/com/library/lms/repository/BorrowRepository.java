package com.library.lms.service;

import com.library.lms.entity.Borrow;
import com.library.lms.repository.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    public Optional<Borrow> getBorrowById(Integer id) {
        // Validate ID before fetching
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return borrowRepository.findById(id);
    }

    public List<Borrow> getAllBorrows() {
        return borrowRepository.findAll();
    }

    public Borrow saveBorrow(Borrow borrow) {
        // Example logic: Validate borrow details
        if (borrow == null || borrow.getUserId() == null || borrow.getBookId() == null) {
            throw new IllegalArgumentException("Invalid borrow details");
        }
        return borrowRepository.save(borrow);
    }

    public void deleteBorrowById(Integer id) {
        if (!borrowRepository.existsById(id)) {
            throw new IllegalArgumentException("Borrow record not found");
        }
        borrowRepository.deleteById(id);
    }
}

// Controller
package com.library.lms.controller;

import com.library.lms.entity.Borrow;
import com.library.lms.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

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
                return ResponseEntity.status(404).body("Borrow record not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Borrow>> getAllBorrows() {
        return ResponseEntity.ok(borrowService.getAllBorrows());
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBorrowById(@PathVariable Integer id) {
        try {
            borrowService.deleteBorrowById(id);
            return ResponseEntity.ok("Borrow record deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

