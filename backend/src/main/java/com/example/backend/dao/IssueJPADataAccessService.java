package com.example.backend.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.backend.entity.issue.IssueEntity;

import lombok.RequiredArgsConstructor;

@Repository("issue-jpa")
@RequiredArgsConstructor
public class IssueJPADataAccessService implements IssueDao {

    private final IssueRepository issueRepository;

    @Override
    public List<IssueEntity> selectAllIssues() {
       return issueRepository.findAll();
    }

    @Override
    public Optional<IssueEntity> selectIssueByIssueId(String issueId) {
        return issueRepository.findByIssueId(issueId);
    }

    @Override
    public void insertIssue(IssueEntity issue) {
        issueRepository.save(issue);
    }

    @Override
    public boolean existsIssueWithIssueId(String issueId) {
        return issueRepository.existsByIssueId(issueId);
    }

    @Override
    public void deleteIssueByIssueId(String issueId) {
        issueRepository.deleteByIssueId(issueId);
    }

    @Override
    public void updateIssue(IssueEntity issue) {
        // issueRepository.update(issue);
        issueRepository.save(issue);
    }
    
}
