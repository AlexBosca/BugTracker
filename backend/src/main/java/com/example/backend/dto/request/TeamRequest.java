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
public class TeamRequest {

//    private String teamId;
    private String name;
//    private Collection<String> projectsIds;
//    private Collection<String> colleaguesIds;
}
