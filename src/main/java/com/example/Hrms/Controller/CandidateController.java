package com.example.Hrms.Controller;

import com.example.Hrms.Entity.Candidate;
import com.example.Hrms.Service.CandidateService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidates")
@CrossOrigin(origins = "*")
public class CandidateController {

    private final CandidateService service;

    public CandidateController(
            CandidateService service
    ) {
        this.service = service;
    }


    // GET ALL CANDIDATES
    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {

        return ResponseEntity.ok(
                service.getAllCandidates()
        );
    }


    // GET CANDIDATE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidate(
            @PathVariable Integer id
    ) {

        return service.getCandidateById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }


    // CREATE CANDIDATE
    @PostMapping
    public ResponseEntity<?> createCandidate(
            @RequestBody Candidate candidate
    ) {

        try {

            Candidate savedCandidate =
                    service.createCandidate(candidate);

            return ResponseEntity.ok(
                    savedCandidate
            );

        } catch (RuntimeException error) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(error.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCandidate(
            @PathVariable Integer id,
            @RequestParam Integer updatedById,
            @RequestBody Candidate candidate
    ) {

        try {

        	Candidate updatedCandidate =
        	        service.updateCandidate(
        	                id,
        	                candidate,
        	                updatedById
        	        );

            return ResponseEntity.ok(
                    updatedCandidate
            );

        } catch (RuntimeException error) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(error.getMessage());
        }
    }


    // DELETE CANDIDATE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(
            @PathVariable Integer id
    ) {

        service.deleteCandidate(id);

        return ResponseEntity.noContent().build();
    }
}