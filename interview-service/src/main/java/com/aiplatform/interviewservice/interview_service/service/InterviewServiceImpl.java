
package com.aiplatform.interviewservice.interview_service.service;

import com.aiplatform.interviewservice.interview_service.entity.Interview;
import com.aiplatform.interviewservice.interview_service.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;

    @Override
    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    @Override
    public Optional<Interview> getInterviewById(Long id) {
        return interviewRepository.findById(id);
    }

    @Override
    public Interview createInterview(Interview interview) {
        return interviewRepository.save(interview);
    }

    @Override
    public Interview updateInterview(Long id, Interview updatedInterview) {
        Interview existing = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found with id: " + id));

        existing.setCandidateId(updatedInterview.getCandidateId());
        existing.setInterviewType(updatedInterview.getInterviewType());
        existing.setScheduledAt(updatedInterview.getScheduledAt());
        existing.setStatus(updatedInterview.getStatus());
        existing.setPanelMembers(updatedInterview.getPanelMembers());

        return interviewRepository.save(existing);
    }

    @Override
    public void deleteInterview(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found with id: " + id));
        interviewRepository.delete(interview);
    }
}
