package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProjectRequest {

    private String projectId;
    private String name;
    private String description;
//    private String teamId;
//    private Collection<String> issuesIds;
}
