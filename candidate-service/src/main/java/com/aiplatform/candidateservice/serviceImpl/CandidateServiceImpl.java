package com.aiplatform.candidateservice.serviceImpl;

import com.aiplatform.candidateservice.Repository.CandidateRepository;
import com.aiplatform.candidateservice.Service.CandidateService;
import com.aiplatform.candidateservice.entity.Candidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    @Override
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Optional<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }

    @Override
    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate updateCandidate(Long id, Candidate updatedCandidate) {
        return candidateRepository.findById(id)
                .map(existingCandidate -> {
                    existingCandidate.setName(updatedCandidate.getName());
                    existingCandidate.setSkills(updatedCandidate.getSkills());
                    // Add any other fields to update
                    return candidateRepository.save(existingCandidate);
                })
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));
    }

    @Override
    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }
}
