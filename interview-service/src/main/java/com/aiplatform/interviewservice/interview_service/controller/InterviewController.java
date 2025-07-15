package com.aiplatform.interviewservice.interview_service.controller;

import com.aiplatform.interviewservice.interview_service.entity.Interview;
import com.aiplatform.interviewservice.interview_service.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
@Tag(name = "Interview API", description = "Operations related to interview scheduling")
public class InterviewController {

    private final InterviewService interviewService;

    @GetMapping
    @Operation(summary = "Get all interviews")
    public ResponseEntity<List<Interview>> getAllInterviews() {
        List<Interview> interviews = interviewService.getAllInterviews();
        if (interviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(interviews);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get interview by ID")
    public ResponseEntity<Interview> getInterviewById(@PathVariable Long id) {
        Optional<Interview> interview = interviewService.getInterviewById(id);
        return interview.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new interview")
    public ResponseEntity<Interview> createInterview(@RequestBody Interview interview) {
        Interview created = interviewService.createInterview(interview);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing interview")
    public ResponseEntity<Interview> updateInterview(@PathVariable Long id, @RequestBody Interview interview) {
        try {
            Interview updated = interviewService.updateInterview(id, interview);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an interview")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        try {
            interviewService.deleteInterview(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
