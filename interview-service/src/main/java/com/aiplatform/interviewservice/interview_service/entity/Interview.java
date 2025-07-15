package com.aiplatform.interviewservice.interview_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;


    private Long candidateId;


    private String candidateName;


    private LocalDateTime scheduledAt; //

    private String interviewType;      //

    private String status;             // at`

    @ElementCollection
    private List<String> panelMembers;

}
