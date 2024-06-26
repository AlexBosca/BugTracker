package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProjectFullResponse {

    private String projectKey;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime targetEndDate;
    private LocalDateTime actualEndDate;
    private Collection<IssueSlimResponse> issues;
    private UserSlimResponse projectManager;
    private Set<UserSlimResponse> assignedUsers;
}
