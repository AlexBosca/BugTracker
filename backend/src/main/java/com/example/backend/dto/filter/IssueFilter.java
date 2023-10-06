package com.example.backend.dto.filter;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IssueFilter {
    private Map<String, Object> filters;
    private Map<String, String> operators;
    private Map<String, String> dataTypes;
}
