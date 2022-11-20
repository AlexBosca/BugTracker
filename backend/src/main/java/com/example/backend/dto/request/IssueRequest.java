package com.example.backend.dto.request;

import com.example.backend.enums.IssuePriority;
import com.example.backend.enums.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Collection;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IssueRequest {

    private String title;
    private String description;
    private String reproducingSteps;
    private String environment;
    private String version;
    private IssuePriority priority;
//    private UserRequest createdByUser;
//    private LocalDateTime createdOn;
//    private UserRequest assignedUser;
//    private LocalDateTime assignedOn;
//    private UserRequest closedByUser;
//    private LocalDateTime closedOn;
//    private Collection<IssueCommentRequest> discussion;
}
