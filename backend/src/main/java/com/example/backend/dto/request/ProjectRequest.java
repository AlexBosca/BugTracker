package com.example.backend.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProjectRequest {
    @NotBlank(message = "project key is mandatory")
    private String projectKey;
    @NotBlank(message = "project name is mandatory")
    private String name;
    @NotBlank(message = "project description is mandatory")
    private String description;
    @NotNull(message = "project start date is mandatory")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @NotNull(message = "project target end date is mandatory")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime targetEndDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualEndDate;
    @NotBlank(message = "project manager id is mandatory")
    private String projectManagerId;
}
