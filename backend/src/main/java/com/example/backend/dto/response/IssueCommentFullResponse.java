package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IssueCommentFullResponse {

    private String comment;
    private LocalDateTime createdOn;
    private UserSlimResponse createdByUser;
    private IssueSlimResponse createdOnIssue;
}
