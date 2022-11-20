package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
//    private Collection<TeamResponse> teams;
//    private Collection<String> issuesCreatedIds;
//    private Collection<IssueRequest> issuesAssignedIds;
//    private Collection<IssueRequest> issuesClosed;
//    private Collection<IssueCommentRequest> commentsCreated;
}
