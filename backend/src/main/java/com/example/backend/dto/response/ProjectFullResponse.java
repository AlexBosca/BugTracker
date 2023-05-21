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
public class ProjectFullResponse {

    private String projectKey;
    private String name;
    private String description;
    private TeamSlimResponse team;
    private Collection<IssueSlimResponse> issues;
}
