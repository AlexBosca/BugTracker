package com.example.backend.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.filter.FilterUtility;
import com.example.backend.entity.issue.IssueEntity;

@Repository("issue-jpa")
public class IssueJPADataAccessService implements IssueDao {

    private final FilterUtility<IssueEntity> filterUtility;
    private final IssueRepository issueRepository;

    public IssueJPADataAccessService(EntityManager entityManager, IssueRepository issueRepository) {
        this.filterUtility = new FilterUtility<>(entityManager, IssueEntity.class);
        this.issueRepository = issueRepository;
    }

    @Override
    public List<IssueEntity> selectAllIssues() {
       return issueRepository.findAll();
    }

    @Override
    public List<IssueEntity> selectAllFilteredIssues(FilterCriteria filterCriteria) {
        return filterUtility.filterEntities(filterCriteria);
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
