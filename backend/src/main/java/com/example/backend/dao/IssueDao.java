package com.example.backend.dao;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.entity.issue.IssueEntity;

import java.util.Optional;
import java.util.List;

public interface IssueDao {
    List<IssueEntity> selectAllIssues();
    List<IssueEntity> selectAllFilteredIssues(FilterCriteria filter);
    Optional<IssueEntity> selectIssueByIssueId(String issueId);
    void insertIssue(IssueEntity issue);
    boolean existsIssueWithIssueId(String issueId);
    void deleteIssueByIssueId(String issueId);
    void updateIssue(IssueEntity issue);
}
