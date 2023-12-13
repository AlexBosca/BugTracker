package com.example.backend.util;

import java.util.Objects;

import org.mockito.ArgumentMatcher;

import com.example.backend.dto.filter.FilterCriteria;

public class FilterCriteriaMatcher implements ArgumentMatcher<FilterCriteria> {
    private final FilterCriteria expectedFilterCriteria;

    public FilterCriteriaMatcher(FilterCriteria expectedFilterCriteria) {
        this.expectedFilterCriteria = expectedFilterCriteria;
    }

    @Override
    public boolean matches(FilterCriteria actualFilterCriteria) {
        return Objects.equals(expectedFilterCriteria.getFilters(), actualFilterCriteria.getFilters()) &&
               Objects.equals(expectedFilterCriteria.getOperators(), actualFilterCriteria.getOperators()) &&
               Objects.equals(expectedFilterCriteria.getDataTypes(), actualFilterCriteria.getDataTypes());
    }
}
