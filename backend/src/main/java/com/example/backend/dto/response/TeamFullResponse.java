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
public class TeamFullResponse {

    private String teamId;
    private String name;
    private Collection<UserSlimResponse> colleagues;
    private Collection<ProjectSlimResponse> projects;
}
