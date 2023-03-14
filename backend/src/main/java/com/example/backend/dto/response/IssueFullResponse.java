package com.example.backend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IssueFullResponse {

    private String issueId;
    private String title;
    private String description;
    private String reproducingSteps;
    private String environment;
    private String version;
    private String status;
    private String priority;
    private UserSlimResponse createdByUser;
    private LocalDateTime createdOn;
    private UserSlimResponse assignedUser;
    private LocalDateTime assignedOn;
    private UserSlimResponse closedByUser;
    private LocalDateTime closedOn;
    private UserSlimResponse tester;
    private Collection<IssueCommentFullResponse> discussion;
    private ProjectSlimResponse project;
}
