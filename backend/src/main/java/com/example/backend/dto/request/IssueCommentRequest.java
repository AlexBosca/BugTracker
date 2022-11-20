package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IssueCommentRequest {

    private String comment;
//    private LocalDateTime createdOn;
//    private String createdByUserWithId;
//    private String createdOnIssueWithId;
}
