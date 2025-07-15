package com.aiplatform.candidateservice.controller;

import com.aiplatform.candidateservice.Service.CandidateService;
import com.aiplatform.candidateservice.dto.CandidateRequest;
import com.aiplatform.candidateservice.entity.Candidate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        log.info("GET /api/candidates called");

        List<Candidate> candidates = candidateService.getAllCandidates();

        if (candidates.isEmpty()) {
            log.warn("No candidates found");
            return ResponseEntity.noContent().build();
        }

        log.info("Returning {} candidates", candidates.size());
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        log.info("GET /api/candidates/{} called", id);

        Optional<Candidate> candidate = candidateService.getCandidateById(id);

        return candidate.map(found -> {
            log.info("Returning candidate: {}", found);
            return ResponseEntity.ok(found);
        }).orElseGet(() -> {
            log.warn("Candidate with ID {} not found", id);
            return ResponseEntity.notFound().build();
        });
    }

    @PostMapping
    public ResponseEntity<Candidate> createCandidate(@Valid @RequestBody CandidateRequest request) {
        log.info("POST /api/candidates called with payload: {}", request);

        Candidate candidate = Candidate.builder()
                .name(request.getName())
                .email(request.getEmail())
                .resumeUrl(request.getResumeUrl())
                .position(request.getPosition())
                .skills(request.getSkills())
                .build();

        Candidate saved = candidateService.createCandidate(candidate);
        log.info("Candidate saved: {}", saved);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(@PathVariable Long id, @RequestBody Candidate updatedCandidate) {
        log.info("PUT /api/candidates/{} called", id);

        try {
            Candidate updated = candidateService.updateCandidate(id, updatedCandidate);
            log.info("Candidate updated: {}", updated);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.warn("Candidate with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        log.info("DELETE /api/candidates/{} called", id);

        try {
            candidateService.deleteCandidate(id);
            log.info("Candidate deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Candidate with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
