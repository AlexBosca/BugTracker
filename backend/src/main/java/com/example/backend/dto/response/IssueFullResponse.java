package com.example.backend.dto.response;


import com.example.backend.enums.IssuePriority;
import com.example.backend.enums.IssueStatus;
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
    private IssueStatus status;
    private IssuePriority priority;
    private UserSlimResponse createdByUser;
    private LocalDateTime createdOn;
    private UserSlimResponse assignedUser;
    private LocalDateTime assignedOn;
    private UserSlimResponse closedByUser;
    private LocalDateTime closedOn;
    private Collection<IssueCommentSlimResponse> discussion;
    private ProjectSlimResponse project;
}
