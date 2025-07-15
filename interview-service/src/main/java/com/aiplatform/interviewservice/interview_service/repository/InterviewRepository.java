package com.aiplatform.interviewservice.interview_service.repository;


import com.aiplatform.interviewservice.interview_service.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview,Long> {


}
