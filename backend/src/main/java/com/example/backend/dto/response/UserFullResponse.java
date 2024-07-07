package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserFullResponse {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private boolean isAccountLocked;
    private boolean isEnabled;
    private String avatarUrl;
    private String phoneNumber;
    private String jobTitle;
    private String department;
    private String timezone;
    private Collection<IssueSlimResponse> issuesCreated;
    private Collection<IssueSlimResponse> issuesAssigned;
    private Collection<IssueSlimResponse> issuesClosed;
    private Collection<IssueCommentSlimResponse> commentsCreated;
}
