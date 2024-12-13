package com.library.lms.service;

import com.library.lms.entity.Logs;
import com.library.lms.repository.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class LogsService {

    @Autowired
    private LogsRepository logsRepository;

    public Optional<Logs> getLogById(Integer id) {
        // Validate ID before fetching
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return logsRepository.findById(id);
    }

    public List<Logs> getAllLogs() {
        return logsRepository.findAll();
    }

    public Logs saveLog(Logs log) {
        // Example logic: Validate log details
        if (log == null || log.getAction() == null || log.getUserId() == null) {
            throw new IllegalArgumentException("Invalid log details");
        }
        return logsRepository.save(log);
    }

    public void deleteLogById(Integer id) {
        if (!logsRepository.existsById(id)) {
            throw new IllegalArgumentException("Log record not found");
        }
        logsRepository.deleteById(id);
    }
}

// Controller
package com.library.lms.controller;

import com.library.lms.entity.Logs;
import com.library.lms.service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

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
                return ResponseEntity.status(404).body("Log record not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Logs>> getAllLogs() {
        return ResponseEntity.ok(logsService.getAllLogs());
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLogById(@PathVariable Integer id) {
        try {
            logsService.deleteLogById(id);
            return ResponseEntity.ok("Log record deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
