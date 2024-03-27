package com.example.backend.dto.request;

import java.time.LocalDateTime;

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
    @NotNull(message = "Project key is mandatory")
    private String projectKey;
    @NotNull(message = "Project name is mandatory")
    private String name;
    @NotNull(message = "Project description is mandatory")
    private String description;
    @NotNull(message = "Project start date is mandatory")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @NotNull(message = "Project target end date is mandatory")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime targetEndDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualEndDate;
}
