package com.aiplatform.interviewservice.interview_service.service;

import com.aiplatform.interviewservice.interview_service.entity.Interview;

import java.util.List;
import java.util.Optional;

public interface InterviewService {

    List<Interview>getAllInterviews();
    Optional<Interview>getInterviewById(Long id);
    Interview createInterview(Interview interview);
    Interview updateInterview(Long id, Interview interview);

    void deleteInterview(Long id);
}
