package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IssueRequest {

    private String issueId;
    private String title;
    private String description;
    private String reproducingSteps;
    private String environment;
    private String version;
    private String priority;
//    private UserRequest createdByUser;
//    private LocalDateTime createdOn;
//    private UserRequest assignedUser;
//    private LocalDateTime assignedOn;
//    private UserRequest closedByUser;
//    private LocalDateTime closedOn;
//    private Collection<IssueCommentRequest> discussion;
}
