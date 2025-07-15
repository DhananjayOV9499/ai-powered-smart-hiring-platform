package com.aiplatform.candidateservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import lombok.Data;

import java.util.List;

@Data
public class CandidateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @URL(message = "Invalid resume URL")
    private String resumeUrl;

    @NotBlank(message = "Position is required")
    private String position;

    private List<String> skills;
}
